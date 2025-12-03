package vetclinic.application;

import vetclinic.application.exceptions.*;
import vetclinic.application.inputDTO.SellMedicationCommand;
import vetclinic.application.mappers.InvoiceMapper;
import vetclinic.application.outputDTO.DiscountUtilizationDTO;
import vetclinic.application.outputDTO.InvoiceHistoryDTO;
import vetclinic.application.outputDTO.InvoiceInformation;
import vetclinic.domain.*;
import vetclinic.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final VisitRepository visitRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationBatchRepository medicationBatchRepository;
    private final LoyaltyTierRepository loyaltyTierRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          VisitRepository visitRepository,
                          PetOwnerRepository petOwnerRepository,
                          MedicationRepository medicationRepository,
                          MedicationBatchRepository medicationBatchRepository,
                          LoyaltyTierRepository loyaltyTierRepository) {
        this.invoiceRepository = invoiceRepository;
        this.visitRepository = visitRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.medicationRepository = medicationRepository;
        this.medicationBatchRepository = medicationBatchRepository;
        this.loyaltyTierRepository = loyaltyTierRepository;
    }

    // UC 3.1: Generate Invoice from Visit
    @Transactional
    public Long generateInvoiceFromVisit(Long visitId) {
        Visit visit = visitRepository.findByIdWithDetails(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        if (visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException("Can only generate invoice for completed visits");
        }

        if (visit.getInvoice() != null) {
            throw new IllegalStateException("Invoice already exists for this visit");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPetOwner(visit.getPetOwner());
        invoice.setVisit(visit);

        // Add visit consultation fee
        InvoiceItem visitItem = new InvoiceItem();
        visitItem.setDescription("Consultation - " + visit.getReasonForVisit());
        visitItem.setQuantity(visit.getDuration() / 15);
        visitItem.setUnitPrice(visit.getPricePerBlock());
        visitItem.setItemType(InvoiceItemType.VISIT);
        visitItem.setReferenceId(visit.getVisitId());
        visitItem.calculateTotal();
        invoice.addItem(visitItem);

        // Add treatments
        for (Treatment treatment : visit.getTreatments()) {
            InvoiceItem treatmentItem = new InvoiceItem();
            treatmentItem.setDescription(treatment.getName());
            treatmentItem.setQuantity(1);
            treatmentItem.setUnitPrice(treatment.getCost());
            treatmentItem.setItemType(InvoiceItemType.TREATMENT);
            treatmentItem.setReferenceId(treatment.getTreatmentId());
            treatmentItem.calculateTotal();
            invoice.addItem(treatmentItem);
        }

        // Add medications
        for (MedicationPrescription prescription : visit.getMedicationPrescriptions()) {
            InvoiceItem medItem = new InvoiceItem();
            medItem.setDescription(prescription.getMedication().getName());
            medItem.setQuantity(prescription.getQuantityPrescribed());
            medItem.setUnitPrice(prescription.getMedication().getUnitPrice());
            medItem.setItemType(InvoiceItemType.MEDICATION);
            medItem.setReferenceId(prescription.getMedication().getMedicationId());
            medItem.calculateTotal();
            invoice.addItem(medItem);
        }

        // Apply loyalty tier discount if applicable
        PetOwner petOwner = visit.getPetOwner();
        if (petOwner.getLoyaltyTier() != null) {
            invoice.applyLoyaltyTierDiscount(
                    petOwner.getLoyaltyTier().getDiscountPercentage()
            );
        }

        Invoice saved = invoiceRepository.save(invoice);
        return saved.getInvoiceId();
    }

    // UC 3.2: Sell Medication/Products (Non-Visit Sale)
    @Transactional
    public Long sellMedication(SellMedicationCommand command) {
        Medication medication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + command.medicationId() + " not found"
                ));

        PetOwner petOwner = null;
        if (command.petOwnerId() != null) {
            petOwner = petOwnerRepository.findByIdWithLoyaltyTier(command.petOwnerId())
                    .orElseThrow(() -> new PetOwnerNotFoundException(
                            "Pet owner with id " + command.petOwnerId() + " not found"
                    ));
        }

        // Find earliest expiring batch
        List<MedicationBatch> batches = medicationBatchRepository
                .findAvailableBatchesByMedicationId(medication.getMedicationId(), LocalDate.now());

        MedicationBatch selectedBatch = null;
        for (MedicationBatch batch : batches) {
            if (batch.getCurrentQuantity() >= command.quantity()) {
                selectedBatch = batch;
                break;
            }
        }

        if (selectedBatch == null) {
            throw new RuntimeException(
                    "Insufficient stock for medication " + medication.getName()
            );
        }

        // Dispense from stock
        selectedBatch.dispense(command.quantity());

        // Create invoice
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        if (petOwner != null) {
            invoice.setPetOwner(petOwner);
        }

        // Add medication item
        InvoiceItem item = new InvoiceItem();
        item.setDescription(medication.getName());
        item.setQuantity(command.quantity());
        item.setUnitPrice(medication.getUnitPrice());
        item.setItemType(InvoiceItemType.MEDICATION);
        item.setReferenceId(medication.getMedicationId());
        item.calculateTotal();

        invoice.addItem(item);

        // UC 3.4: Apply Loyalty Tier Discount (if applicable)
        if (petOwner != null && petOwner.getLoyaltyTier() != null) {
            invoice.applyLoyaltyTierDiscount(
                    petOwner.getLoyaltyTier().getDiscountPercentage()
            );
        }

        Invoice saved = invoiceRepository.save(invoice);
        return saved.getInvoiceId();
    }

    // UC 3.5: Earn Fidelity Points (called when invoice is paid)
    @Transactional
    public void earnFidelityPoints(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException(
                        "Invoice with id " + invoiceId + " not found"
                ));

        if (invoice.getStatus() != InvoiceStatus.PAID) {
            throw new IllegalStateException("Can only earn points for paid invoices");
        }

        PetOwner petOwner = invoice.getPetOwner();
        if (petOwner == null) {
            return;
        }

        // Calculate points: 1 point per 10€ spent
        int pointsEarned = (int) (invoice.getFinalAmount() / 10.0);
        petOwner.addLoyaltyPoints(pointsEarned);

        // Update loyalty tier based on points
        updateLoyaltyTier(petOwner);

        petOwnerRepository.save(petOwner);
    }

    public InvoiceInformation getInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdWithDetails(invoiceId)
                .orElseThrow(() -> new RuntimeException(
                        "Invoice with id " + invoiceId + " not found"
                ));

        return InvoiceMapper.toInformation(invoice);
    }

    // UC 3.10: View Invoice History
    @Transactional(readOnly = true)
    public List<InvoiceHistoryDTO> getInvoiceHistory(Long petOwnerId, LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices;

        if (petOwnerId != null) {
            invoices = invoiceRepository.findByPetOwnerAndDateRange(
                    petOwnerId, startDate, endDate
            );
        } else {
            invoices = invoiceRepository.findByDateRange(startDate, endDate);
        }

        return invoices.stream()
                .map(this::mapToInvoiceHistory)
                .collect(Collectors.toList());
    }

    // UC 3.12: Analyze Discount Utilization
    @Transactional(readOnly = true)
    public List<DiscountUtilizationDTO> analyzeDiscountUtilization(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findDiscountUtilization(startDate, endDate);
    }

    // Private helper methods
    private void updateLoyaltyTier(PetOwner petOwner) {
        List<LoyaltyTier> tiers = (List<LoyaltyTier>) loyaltyTierRepository.findAll();

        LoyaltyTier appropriateTier = tiers.stream()
                .filter(t -> petOwner.getLoyaltyPoints() >= t.getRequiredPoints())
                .max((t1, t2) -> t1.getRequiredPoints().compareTo(t2.getRequiredPoints()))
                .orElse(null);

        if (appropriateTier != null &&
                (petOwner.getLoyaltyTier() == null ||
                        !petOwner.getLoyaltyTier().equals(appropriateTier))) {
            petOwner.setLoyaltyTier(appropriateTier);
        }
    }

    private InvoiceHistoryDTO mapToInvoiceHistory(Invoice invoice) {
        String ownerName = invoice.getPetOwner() != null ?
                invoice.getPetOwner().getFirstName() + " " + invoice.getPetOwner().getLastName() :
                "Unknown";

        return new InvoiceHistoryDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceDate(),
                ownerName,
                invoice.getTotalAmount(),
                invoice.getDiscountAmount(),
                invoice.getFinalAmount(),
                invoice.getStatus().name()
        );
    }
}
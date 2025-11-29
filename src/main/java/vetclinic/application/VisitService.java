package vetclinic.application;

import vetclinic.application.exceptions.*;
import vetclinic.application.inputDTO.*;
import vetclinic.application.mappers.*;
import vetclinic.application.outputDTO.*;
import vetclinic.domain.*;
import vetclinic.domain.exceptions.TimeSlotNotAvailableException;
import vetclinic.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationBatchRepository medicationBatchRepository;
    private final TreatmentRepository treatmentRepository;
    private final MedicationPrescriptionRepository medicationPrescriptionRepository;

    public VisitService(VisitRepository visitRepository,
                        PetRepository petRepository,
                        PetOwnerRepository petOwnerRepository,
                        VeterinarianRepository veterinarianRepository,
                        MedicationRepository medicationRepository,
                        MedicationBatchRepository medicationBatchRepository,
                        TreatmentRepository treatmentRepository,
                        MedicationPrescriptionRepository medicationPrescriptionRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.medicationRepository = medicationRepository;
        this.medicationBatchRepository = medicationBatchRepository;
        this.treatmentRepository = treatmentRepository;
        this.medicationPrescriptionRepository = medicationPrescriptionRepository;
    }

    // UC 1.1: Schedule New Visit
    @Transactional
    public Long scheduleVisit(ScheduleVisitCommand command) {
        Pet pet = petRepository.findById(command.petId())
                .orElseThrow(() -> new PetNotFoundException(
                        "Pet with id " + command.petId() + " not found"
                ));

        PetOwner petOwner = petOwnerRepository.findById(command.petOwnerId())
                .orElseThrow(() -> new PetOwnerNotFoundException(
                        "Pet owner with id " + command.petOwnerId() + " not found"
                ));

        Veterinarian veterinarian = veterinarianRepository.findById(command.veterinarianId())
                .orElseThrow(() -> new VeterinarianNotFoundException(
                        "Veterinarian with id " + command.veterinarianId() + " not found"
                ));

        // Check if slot is available
        if (!isTimeSlotAvailable(command.veterinarianId(), command.dateTime(), command.duration())) {
            throw new TimeSlotNotAvailableException(
                    "Time slot not available for veterinarian " + command.veterinarianId()
            );
        }

        // Validate duration is multiple of 15
        if (command.duration() % 15 != 0) {
            throw new IllegalArgumentException("Duration must be a multiple of 15 minutes");
        }

        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setPetOwner(petOwner);
        visit.setVeterinarian(veterinarian);
        visit.setDateTime(command.dateTime());
        visit.setDuration(command.duration());
        visit.setReasonForVisit(command.reasonForVisit());
        visit.setStatus(VisitStatus.SCHEDULED);

        Visit saved = visitRepository.save(visit);
        return saved.getVisitId();
    }

    // UC 1.2: View Veterinarian's Schedule
    public ScheduleInformation getVeterinarianSchedule(Long veterinarianId, LocalDate date) {
        Veterinarian veterinarian = veterinarianRepository.findByIdWithAvailabilities(veterinarianId)
                .orElseThrow(() -> new VeterinarianNotFoundException(
                        "Veterinarian with id " + veterinarianId + " not found"
                ));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Visit> visits = visitRepository.findByVeterinarianAndDateRange(
                veterinarianId, startOfDay, endOfDay
        );

        List<TimeSlotInformation> slots = generateTimeSlots(veterinarian, date, visits);

        return new ScheduleInformation(
                veterinarianId,
                veterinarian.getFirstName() + " " + veterinarian.getLastName(),
                date,
                slots
        );
    }

    // UC 2.1: Start/Complete Visit Consultation
    @Transactional
    public void startConsultation(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        visit.startConsultation();
        visitRepository.save(visit);
    }

    @Transactional
    public void completeConsultation(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        visit.completeConsultation();
        visitRepository.save(visit);
    }

    // UC 2.2: Record Visit Diagnosis and Notes
    @Transactional
    public void recordDiagnosis(Long visitId, RecordDiagnosisCommand command) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        if (visit.getStatus() != VisitStatus.IN_PROGRESS &&
                visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Can only record diagnosis for visits in progress or completed"
            );
        }

        visit.setDiagnosis(command.diagnosis());
        visit.setNotes(command.notes());
        visitRepository.save(visit);
    }

    // UC 2.3: Prescribe Medication
    @Transactional
    public Long prescribeMedication(Long visitId, PrescribeMedicationCommand command) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        if (visit.getStatus() != VisitStatus.IN_PROGRESS &&
                visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Can only prescribe medication for visits in progress or completed"
            );
        }

        Medication medication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + command.medicationId() + " not found"
                ));

        // Find earliest expiring batch with sufficient quantity
        List<MedicationBatch> batches = medicationBatchRepository
                .findAvailableBatchesByMedicationId(medication.getMedicationId(), LocalDate.now());

        MedicationBatch selectedBatch = null;
        for (MedicationBatch batch : batches) {
            if (batch.getCurrentQuantity() >= command.quantityPrescribed()) {
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
        selectedBatch.dispense(command.quantityPrescribed());

        MedicationPrescription prescription = new MedicationPrescription();
        prescription.setMedication(medication);
        prescription.setBatch(selectedBatch);
        prescription.setQuantityPrescribed(command.quantityPrescribed());
        prescription.setDosageInstructions(command.dosageInstructions());
        prescription.setDuration(command.duration());

        visit.addMedicationPrescription(prescription);

        MedicationPrescription saved = medicationPrescriptionRepository.save(prescription);
        return saved.getPrescriptionId();
    }

    // UC 2.4: Prescribe/Record Treatment
    @Transactional
    public Long prescribeTreatment(Long visitId, PrescribeTreatmentCommand command) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        if (visit.getStatus() != VisitStatus.IN_PROGRESS &&
                visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Can only prescribe treatment for visits in progress or completed"
            );
        }

        Treatment treatment = new Treatment();
        treatment.setName(command.name());
        treatment.setDescription(command.description());
        treatment.setCost(command.cost());

        visit.addTreatment(treatment);

        Treatment saved = treatmentRepository.save(treatment);
        return saved.getTreatmentId();
    }

    // Get visit details
    public VisitInformation getVisit(Long visitId) {
        Visit visit = visitRepository.findByIdWithDetails(visitId)
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit with id " + visitId + " not found"
                ));

        return VisitMapper.toInformation(visit);
    }

    // Helper methods
    private boolean isTimeSlotAvailable(Long veterinarianId, LocalDateTime dateTime, Integer duration) {
        LocalDateTime endTime = dateTime.plusMinutes(duration);

        List<Visit> overlappingVisits = visitRepository.findByVeterinarianAndDateRange(
                veterinarianId, dateTime.minusMinutes(1), endTime.plusMinutes(1)
        );

        for (Visit visit : overlappingVisits) {
            if (visit.getStatus() != VisitStatus.CANCELLED &&
                    visit.getStatus() != VisitStatus.NOT_SHOWED_UP) {
                return false;
            }
        }

        return true;
    }

    private List<TimeSlotInformation> generateTimeSlots(
            Veterinarian veterinarian, LocalDate date, List<Visit> visits) {

        List<TimeSlotInformation> slots = new ArrayList<>();

        // Find availability for this day
        Availability availability = veterinarian.getAvailabilities().stream()
                .filter(a -> a.isAvailableOnDate(date))
                .findFirst()
                .orElse(null);

        if (availability == null) {
            return slots; // No availability for this day
        }

        LocalTime currentTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();

        while (currentTime.isBefore(endTime)) {
            LocalTime slotEnd = currentTime.plusMinutes(15);
            LocalDateTime slotDateTime = date.atTime(currentTime);

            Visit visitInSlot = visits.stream()
                    .filter(v -> v.getDateTime().equals(slotDateTime))
                    .filter(v -> v.getStatus() != VisitStatus.CANCELLED &&
                            v.getStatus() != VisitStatus.NOT_SHOWED_UP)
                    .findFirst()
                    .orElse(null);

            slots.add(new TimeSlotInformation(
                    currentTime,
                    slotEnd,
                    visitInSlot == null,
                    visitInSlot != null ? visitInSlot.getVisitId() : null
            ));

            currentTime = slotEnd;
        }

        return slots;
    }
}
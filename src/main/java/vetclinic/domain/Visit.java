package vetclinic.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    private LocalDateTime dateTime;
    private Integer duration = 15; // default 15 minutes
    private String reasonForVisit;
    private Double pricePerBlock = 20.0; // price per 15 minutes

    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.SCHEDULED;

    // VPR fields
    private String diagnosis;
    private String notes;

    @OneToMany(
            mappedBy = "visit",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Treatment> treatments = new ArrayList<>();

    @OneToMany(
            mappedBy = "visit",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MedicationPrescription> medicationPrescriptions = new ArrayList<>();

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Invoice invoice;

    public Visit() {
    }

    // Business logic
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
        treatment.setVisit(this);
    }

    public void removeTreatment(Treatment treatment) {
        treatments.remove(treatment);
        treatment.setVisit(null);
    }

    public void addMedicationPrescription(MedicationPrescription prescription) {
        medicationPrescriptions.add(prescription);
        prescription.setVisit(this);
    }

    public void startConsultation() {
        if (this.status != VisitStatus.SCHEDULED) {
            throw new IllegalStateException("Can only start scheduled visits");
        }
        this.status = VisitStatus.IN_PROGRESS;
    }

    public void completeConsultation() {
        if (this.status != VisitStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete visits in progress");
        }
        this.status = VisitStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status != VisitStatus.SCHEDULED) {
            throw new IllegalStateException("Can only cancel scheduled visits");
        }
        this.status = VisitStatus.CANCELLED;
    }

    public void markNoShow() {
        if (this.status != VisitStatus.SCHEDULED) {
            throw new IllegalStateException("Can only mark scheduled visits as no-show");
        }
        this.status = VisitStatus.NOT_SHOWED_UP;
    }

    public Double calculateTotalCost() {
        int blocks = duration / 15;
        return blocks * pricePerBlock;
    }

    // Getters and Setters
    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public Double getPricePerBlock() {
        return pricePerBlock;
    }

    public void setPricePerBlock(Double pricePerBlock) {
        this.pricePerBlock = pricePerBlock;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Treatment> getTreatments() {
        return List.copyOf(treatments);
    }

    public List<MedicationPrescription> getMedicationPrescriptions() {
        return List.copyOf(medicationPrescriptions);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
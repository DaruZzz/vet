package vetclinic.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicationId;

    @Column(unique = true)
    private String name;

    private String activeIngredient;
    private String dosageUnit;
    private Double unitPrice;
    private Integer reorderThreshold;

    @OneToMany(
            mappedBy = "medication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MedicationBatch> batches = new ArrayList<>();

    public Medication() {
    }

    // Business logic
    public void addBatch(MedicationBatch batch) {
        batches.add(batch);
        batch.setMedication(this);
    }

    public Integer getTotalCurrentQuantity() {
        return batches.stream()
                .mapToInt(MedicationBatch::getCurrentQuantity)
                .sum();
    }

    public boolean isLowStock() {
        return getTotalCurrentQuantity() < reorderThreshold;
    }

    // Getters and Setters
    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(Integer reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public List<MedicationBatch> getBatches() {
        return List.copyOf(batches);
    }
}
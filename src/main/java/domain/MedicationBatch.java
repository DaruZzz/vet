package domain;

import domain.exceptions.BatchExpiredException;
import domain.exceptions.InsufficientStockException;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MedicationBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    private String lotNumber;
    private LocalDate receivedDate;
    private LocalDate expiryDate;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private Double purchasePricePerUnit;
    private String storageLocation;

    public MedicationBatch() {
    }

    // Business logic
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isDepleted() {
        return currentQuantity == 0;
    }

    public void dispense(Integer quantity) {
        if (isExpired()) {
            throw new BatchExpiredException("Batch " + lotNumber + " has expired");
        }

        if (currentQuantity < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock in batch " + lotNumber +
                            ". Available: " + currentQuantity + ", Requested: " + quantity
            );
        }

        currentQuantity -= quantity;
    }

    // Getters and Setters
    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Double getPurchasePricePerUnit() {
        return purchasePricePerUnit;
    }

    public void setPurchasePricePerUnit(Double purchasePricePerUnit) {
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
package vetclinic.domain;

import jakarta.persistence.*;

@Entity
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String description;
    private Integer quantity;
    private Double unitPrice;
    private Double itemTotal;

    @Enumerated(EnumType.STRING)
    private InvoiceItemType itemType;

    // References to source entities
    private Long referenceId; // Can be treatmentId, medicationId, etc.

    public InvoiceItem() {
    }

    public InvoiceItem(String description, Integer quantity, Double unitPrice, InvoiceItemType itemType) {
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemType = itemType;
        calculateTotal();
    }

    // Business logic
    public void calculateTotal() {
        this.itemTotal = quantity * unitPrice;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public Double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public InvoiceItemType getItemType() {
        return itemType;
    }

    public void setItemType(InvoiceItemType itemType) {
        this.itemType = itemType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
}
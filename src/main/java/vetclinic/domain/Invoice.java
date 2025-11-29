package vetclinic.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    private LocalDate invoiceDate;
    private Double totalAmount = 0.0;
    private Double discountAmount = 0.0;
    private Double finalAmount = 0.0;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL
    )
    private List<Payment> payments = new ArrayList<>();

    public Invoice() {
    }

    // Business logic
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
        recalculateTotals();
    }

    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
        recalculateTotals();
    }

    public void applyDiscount(Double discount) {
        this.discountAmount += discount;
        recalculateTotals();
    }

    public void applyLoyaltyTierDiscount(Double percentage) {
        Double discount = totalAmount * (percentage / 100.0);
        applyDiscount(discount);
    }

    private void recalculateTotals() {
        this.totalAmount = items.stream()
                .mapToDouble(InvoiceItem::getItemTotal)
                .sum();
        this.finalAmount = Math.max(0, totalAmount - discountAmount);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this);
        updatePaymentStatus();
    }

    private void updatePaymentStatus() {
        Double totalPaid = payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        if (totalPaid >= finalAmount) {
            this.status = InvoiceStatus.PAID;
        } else if (totalPaid > 0) {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            this.status = InvoiceStatus.UNPAID;
        }
    }

    // Getters and Setters
    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public List<InvoiceItem> getItems() {
        return List.copyOf(items);
    }

    public List<Payment> getPayments() {
        return List.copyOf(payments);
    }
}
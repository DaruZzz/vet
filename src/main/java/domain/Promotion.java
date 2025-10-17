package domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    private String name;
    private String description;
    private String discountCode;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(
            mappedBy = "promotion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Discount> discounts = new ArrayList<>();

    public Promotion() {
    }

    // Business logic
    public void addDiscount(Discount discount) {
        discounts.add(discount);
        discount.setPromotion(this);
    }

    public void removeDiscount(Discount discount) {
        discounts.remove(discount);
        discount.setPromotion(null);
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    // Getters and Setters
    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Discount> getDiscounts() {
        return List.copyOf(discounts);
    }
}
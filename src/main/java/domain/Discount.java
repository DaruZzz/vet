package domain;

import domain.exceptions.DiscountExpiredException;
import domain.exceptions.DiscountMaxUsesExceededException;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    private Double value; // Percentage or fixed amount
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxUses;
    private Integer usesCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_tier_id")
    private LoyaltyTier loyaltyTier;

    public Discount() {
    }

    // Business logic
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return now.isAfter(startDate) && now.isBefore(endDate) &&
                (maxUses == null || usesCount < maxUses);
    }

    public void incrementUsesCount() {
        if (maxUses != null && usesCount >= maxUses) {
            throw new DiscountMaxUsesExceededException(
                    "Discount " + code + " has reached maximum uses"
            );
        }

        if (!isValid()) {
            throw new DiscountExpiredException("Discount " + code + " is expired or not yet valid");
        }

        usesCount++;
    }

    public Double calculateDiscount(Double amount) {
        if (!isValid()) {
            return 0.0;
        }

        return switch (type) {
            case PERCENTAGE -> amount * (value / 100.0);
            case FIXED_AMOUNT -> Math.min(value, amount);
            case LOYALTY_TIER -> amount * (value / 100.0);
        };
    }

    // Getters and Setters
    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getUsesCount() {
        return usesCount;
    }

    public void setUsesCount(Integer usesCount) {
        this.usesCount = usesCount;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public LoyaltyTier getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(LoyaltyTier loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }
}
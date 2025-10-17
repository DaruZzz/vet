package vetclinic.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LoyaltyTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tierId;

    @Column(unique = true)
    private String tierName;

    private Integer requiredPoints;
    private Double discountPercentage;
    private String benefitsDescription;

    @OneToMany(
            mappedBy = "loyaltyTier",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Discount> discounts = new ArrayList<>();

    public LoyaltyTier() {
    }

    // Business logic
    public void addDiscount(Discount discount) {
        discounts.add(discount);
        discount.setLoyaltyTier(this);
    }

    public void removeDiscount(Discount discount) {
        discounts.remove(discount);
        discount.setLoyaltyTier(null);
    }

    // Getters and Setters
    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getBenefitsDescription() {
        return benefitsDescription;
    }

    public void setBenefitsDescription(String benefitsDescription) {
        this.benefitsDescription = benefitsDescription;
    }

    public List<Discount> getDiscounts() {
        return List.copyOf(discounts);
    }
}
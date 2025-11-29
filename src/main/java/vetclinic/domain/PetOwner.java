package vetclinic.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PetOwner extends Person {

    private Integer loyaltyPoints = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_tier_id")
    private LoyaltyTier loyaltyTier;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(
            mappedBy = "petOwner",
            cascade = CascadeType.ALL
    )
    private List<Invoice> invoices = new ArrayList<>();

    public PetOwner() {
        super();
    }

    // Business logic
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setOwner(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setOwner(null);
    }

    public void addLoyaltyPoints(Integer points) {
        this.loyaltyPoints += points;
        updateLoyaltyTier();
    }

    public void deductLoyaltyPoints(Integer points) {
        if (this.loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
            updateLoyaltyTier();
        } else {
            throw new IllegalArgumentException("Insufficient loyalty points");
        }
    }

    private void updateLoyaltyTier() {
        // This will be handled by service layer with proper tier lookup
    }

    // Getters and Setters
    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public LoyaltyTier getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(LoyaltyTier loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public List<Pet> getPets() {
        return List.copyOf(pets);
    }

    public List<Invoice> getInvoices() {
        return List.copyOf(invoices);
    }
}
package vetclinic.application;

import vetclinic.application.exceptions.PetOwnerNotFoundException;
import vetclinic.application.outputDTO.FidelityPointsBalanceDTO;
import vetclinic.domain.LoyaltyTier;
import vetclinic.domain.PetOwner;
import vetclinic.persistence.LoyaltyTierRepository;
import vetclinic.persistence.PetOwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;
    private final LoyaltyTierRepository loyaltyTierRepository;

    public PetOwnerService(PetOwnerRepository petOwnerRepository,
                           LoyaltyTierRepository loyaltyTierRepository) {
        this.petOwnerRepository = petOwnerRepository;
        this.loyaltyTierRepository = loyaltyTierRepository;
    }

    // UC 3.11: View Fidelity Point Balance
    @Transactional(readOnly = true)
    public FidelityPointsBalanceDTO getFidelityPointsBalance(Long petOwnerId) {
        PetOwner petOwner = petOwnerRepository.findByIdWithLoyaltyTier(petOwnerId)
                .orElseThrow(() -> new PetOwnerNotFoundException(
                        "Pet owner with id " + petOwnerId + " not found"
                ));

        String currentTierName = petOwner.getLoyaltyTier() != null ?
                petOwner.getLoyaltyTier().getTierName() : "None";

        // Find next tier
        List<LoyaltyTier> allTiers = (List<LoyaltyTier>) loyaltyTierRepository.findAll();
        allTiers.sort(Comparator.comparing(LoyaltyTier::getRequiredPoints));

        String nextTierName = null;
        Integer pointsToNextTier = null;

        for (LoyaltyTier tier : allTiers) {
            if (tier.getRequiredPoints() > petOwner.getLoyaltyPoints()) {
                nextTierName = tier.getTierName();
                pointsToNextTier = tier.getRequiredPoints() - petOwner.getLoyaltyPoints();
                break;
            }
        }

        return new FidelityPointsBalanceDTO(
                petOwner.getPersonId(),
                petOwner.getFirstName() + " " + petOwner.getLastName(),
                petOwner.getLoyaltyPoints(),
                currentTierName,
                nextTierName,
                pointsToNextTier
        );
    }
}
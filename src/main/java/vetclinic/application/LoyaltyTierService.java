package vetclinic.application;

import vetclinic.application.exceptions.LoyaltyTierNotFoundException;
import vetclinic.application.inputDTO.LoyaltyTierCommand;
import vetclinic.application.mappers.LoyaltyTierMapper;
import vetclinic.application.outputDTO.LoyaltyTierInformation;
import vetclinic.domain.LoyaltyTier;
import vetclinic.persistence.LoyaltyTierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LoyaltyTierService {

    private final LoyaltyTierRepository loyaltyTierRepository;

    public LoyaltyTierService(LoyaltyTierRepository loyaltyTierRepository) {
        this.loyaltyTierRepository = loyaltyTierRepository;
    }

    // UC 3.9: Define Loyalty Tiers
    @Transactional
    public Long createLoyaltyTier(LoyaltyTierCommand command) {
        LoyaltyTier tier = LoyaltyTierMapper.commandToDomain(command);
        LoyaltyTier saved = loyaltyTierRepository.save(tier);
        return saved.getTierId();
    }

    @Transactional
    public void updateLoyaltyTier(Long tierId, LoyaltyTierCommand command) {
        LoyaltyTier tier = loyaltyTierRepository.findById(tierId)
                .orElseThrow(() -> new LoyaltyTierNotFoundException(
                        "Loyalty tier with id " + tierId + " not found"
                ));

        tier.setTierName(command.tierName());
        tier.setRequiredPoints(command.requiredPoints());
        tier.setDiscountPercentage(command.discountPercentage());
        tier.setBenefitsDescription(command.benefitsDescription());

        loyaltyTierRepository.save(tier);
    }

    @Transactional
    public void deleteLoyaltyTier(Long tierId) {
        LoyaltyTier tier = loyaltyTierRepository.findById(tierId)
                .orElseThrow(() -> new LoyaltyTierNotFoundException(
                        "Loyalty tier with id " + tierId + " not found"
                ));

        loyaltyTierRepository.delete(tier);
    }

    public LoyaltyTierInformation getLoyaltyTier(Long tierId) {
        LoyaltyTier tier = loyaltyTierRepository.findById(tierId)
                .orElseThrow(() -> new LoyaltyTierNotFoundException(
                        "Loyalty tier with id " + tierId + " not found"
                ));

        return LoyaltyTierMapper.toInformation(tier);
    }

    public List<LoyaltyTierInformation> getAllLoyaltyTiers() {
        return StreamSupport.stream(loyaltyTierRepository.findAll().spliterator(), false)
                .map(LoyaltyTierMapper::toInformation)
                .collect(Collectors.toList());
    }
}
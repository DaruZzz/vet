package vetclinic.application.mappers;

import vetclinic.application.inputDTO.LoyaltyTierCommand;
import vetclinic.application.outputDTO.LoyaltyTierInformation;
import vetclinic.domain.LoyaltyTier;

public class LoyaltyTierMapper {

    public static LoyaltyTier commandToDomain(LoyaltyTierCommand command) {
        LoyaltyTier tier = new LoyaltyTier();
        tier.setTierName(command.tierName());
        tier.setRequiredPoints(command.requiredPoints());
        tier.setDiscountPercentage(command.discountPercentage());
        tier.setBenefitsDescription(command.benefitsDescription());
        return tier;
    }

    public static LoyaltyTierInformation toInformation(LoyaltyTier tier) {
        return new LoyaltyTierInformation(
                tier.getTierId(),
                tier.getTierName(),
                tier.getRequiredPoints(),
                tier.getDiscountPercentage(),
                tier.getBenefitsDescription()
        );
    }
}

package vetclinic.application.outputDTO;

public record LoyaltyTierInformation(
        Long tierId,
        String tierName,
        Integer requiredPoints,
        Double discountPercentage,
        String benefitsDescription
) {}

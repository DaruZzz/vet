package vetclinic.application.outputDTO;

public record FidelityPointsBalanceDTO(
        Long petOwnerId,
        String ownerName,
        Integer currentPoints,
        String currentTier,
        String nextTier,
        Integer pointsToNextTier
) {}
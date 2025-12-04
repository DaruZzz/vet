package vetclinic.application.outputDTO;

public record MedicationIncompatibilityDTO(
        Long incompatibilityId,
        Long medication1Id,
        String medication1Name,
        Long medication2Id,
        String medication2Name,
        Integer persistingPeriodDays
) {}
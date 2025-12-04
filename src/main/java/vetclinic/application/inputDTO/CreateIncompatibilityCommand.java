package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateIncompatibilityCommand(
        @NotNull Long medication1Id,
        @NotNull Long medication2Id,
        @Positive Integer persistingPeriodDays // null = siempre incompatibles
) {}
package vetclinic.application.inputDTO;

import jakarta.validation.constraints.Positive;

public record UpdateIncompatibilityCommand(
        @Positive Integer persistingPeriodDays
) {}
package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record LoyaltyTierCommand(
        @NotBlank String tierName,
        @NotNull @PositiveOrZero Integer requiredPoints,
        @NotNull @Positive Double discountPercentage,
        String benefitsDescription
) {}

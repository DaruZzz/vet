package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RedeemPointsCommand(
        @NotNull @Positive Integer pointsToRedeem
) {}
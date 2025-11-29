package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SellMedicationCommand(
        @NotNull Long medicationId,
        @NotNull @Positive Integer quantity,
        Long petOwnerId // Optional, for loyalty points
) {}
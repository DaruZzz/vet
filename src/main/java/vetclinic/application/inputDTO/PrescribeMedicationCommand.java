package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrescribeMedicationCommand(
        @NotNull Long medicationId,
        @NotNull @Positive Integer quantityPrescribed,
        @NotNull String dosageInstructions,
        @NotNull String duration
) {}
package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrescribeTreatmentCommand(
        @NotBlank String name,
        String description,
        @NotNull @Positive Double cost
) {}
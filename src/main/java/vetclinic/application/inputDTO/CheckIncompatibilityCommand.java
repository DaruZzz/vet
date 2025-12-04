package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;

public record CheckIncompatibilityCommand(
        @NotNull Long petId,
        @NotNull Long medicationId
) {}
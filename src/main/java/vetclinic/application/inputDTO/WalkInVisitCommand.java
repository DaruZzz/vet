package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;

public record WalkInVisitCommand(
        @NotNull Long petId,
        @NotNull Long petOwnerId,
        @NotNull Long veterinarianId,
        @NotNull String reasonForVisit,
        Integer duration // Optional, defaults to 15
) {}
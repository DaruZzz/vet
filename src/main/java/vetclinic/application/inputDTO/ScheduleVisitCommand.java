package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record ScheduleVisitCommand(
        @NotNull Long petId,
        @NotNull Long petOwnerId,
        @NotNull Long veterinarianId,
        @NotNull LocalDateTime dateTime,
        @Positive Integer duration, // in minutes, multiples of 15
        @NotNull String reasonForVisit
) {}
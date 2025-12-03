package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RescheduleVisitCommand(
        @NotNull LocalDateTime newDateTime,
        Integer newDuration // Optional, if null keep original
) {}
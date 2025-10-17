package application.inputDTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AvailabilityExceptionCommand(
        @NotNull LocalDate exceptionDate,
        LocalTime startTime,
        LocalTime endTime,
        String reason
) {}

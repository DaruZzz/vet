package application.outputDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record AvailabilityExceptionInformation(
        Long exceptionId,
        LocalDate exceptionDate,
        LocalTime startTime,
        LocalTime endTime,
        String reason
) {}

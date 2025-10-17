package application.outputDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record AvailabilityInformation(
        Long availabilityId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate initialDate,
        LocalDate finalDate
) {}

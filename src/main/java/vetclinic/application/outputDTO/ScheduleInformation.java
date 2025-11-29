package vetclinic.application.outputDTO;

import java.time.LocalDate;
import java.util.List;

public record ScheduleInformation(
        Long veterinarianId,
        String veterinarianName,
        LocalDate date,
        List<TimeSlotInformation> slots
) {}
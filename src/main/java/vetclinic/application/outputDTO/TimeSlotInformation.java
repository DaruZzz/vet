package vetclinic.application.outputDTO;

import java.time.LocalTime;

public record TimeSlotInformation(
        LocalTime startTime,
        LocalTime endTime,
        boolean available,
        Long visitId
) {}
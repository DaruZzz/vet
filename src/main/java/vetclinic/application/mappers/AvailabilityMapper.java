package vetclinic.application.mappers;

import vetclinic.application.inputDTO.AvailabilityCommand;
import vetclinic.application.outputDTO.AvailabilityInformation;
import vetclinic.domain.Availability;

public class AvailabilityMapper {

    public static Availability commandToDomain(AvailabilityCommand command) {
        Availability availability = new Availability();
        availability.setDayOfWeek(command.dayOfWeek());
        availability.setStartTime(command.startTime());
        availability.setEndTime(command.endTime());
        availability.setInitialDate(command.initialDate());
        availability.setFinalDate(command.finalDate());
        return availability;
    }

    public static AvailabilityInformation toInformation(Availability availability) {
        return new AvailabilityInformation(
                availability.getAvailabilityId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.getInitialDate(),
                availability.getFinalDate()
        );
    }
}

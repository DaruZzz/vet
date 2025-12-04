package vetclinic.application.outputDTO;

import java.time.LocalDateTime;

public record IncompatibilityAlertDTO(
        String conflictingMedicationName,
        String reason,
        LocalDateTime prescribedDate,
        Integer daysRemaining,
        boolean isActive
) {}
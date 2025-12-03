package vetclinic.application.outputDTO;

import java.time.LocalDateTime;
import java.util.List;

public record MedicalEventDTO(
        Long visitId,
        LocalDateTime dateTime,
        String veterinarianName,
        String reasonForVisit,
        String diagnosis,
        String notes,
        String status,
        List<String> treatments,
        List<String> medications
) {}
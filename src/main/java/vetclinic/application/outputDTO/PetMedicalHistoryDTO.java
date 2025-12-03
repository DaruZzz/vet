package vetclinic.application.outputDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PetMedicalHistoryDTO(
        Long petId,
        String petName,
        String petType,
        String ownerName,
        List<MedicalEventDTO> medicalHistory
) {}
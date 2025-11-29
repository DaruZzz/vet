package vetclinic.application.outputDTO;

import java.time.LocalDateTime;
import java.util.List;

public record VisitInformation(
        Long visitId,
        Long petId,
        String petName,
        Long petOwnerId,
        String petOwnerName,
        Long veterinarianId,
        String veterinarianName,
        LocalDateTime dateTime,
        Integer duration,
        String reasonForVisit,
        String status,
        String diagnosis,
        String notes,
        Double totalCost,
        List<TreatmentInformation> treatments,
        List<MedicationPrescriptionInformation> prescriptions
) {}
package vetclinic.application.outputDTO;

public record MedicationPrescriptionStatsDTO(
        Long medicationId,
        String medicationName,
        Long prescriptionCount
) {}
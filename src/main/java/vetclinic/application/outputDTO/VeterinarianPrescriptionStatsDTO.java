package vetclinic.application.outputDTO;

public record VeterinarianPrescriptionStatsDTO(
        Long veterinarianId,
        String veterinarianName,
        Long prescriptionCount
) {}
package vetclinic.application.outputDTO;

public record VeterinarianDemandDTO(
        Long veterinarianId,
        String veterinarianName,
        Long visitCount
) {}
package vetclinic.application.outputDTO;

public record SpecialityDemandDTO(
        Long specialityId,
        String specialityName,
        Long visitCount
) {}
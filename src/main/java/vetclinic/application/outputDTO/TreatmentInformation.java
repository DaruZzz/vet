package vetclinic.application.outputDTO;

public record TreatmentInformation(
        Long treatmentId,
        String name,
        String description,
        Double cost
) {}
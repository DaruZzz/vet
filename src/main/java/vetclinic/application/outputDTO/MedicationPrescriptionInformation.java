package vetclinic.application.outputDTO;

public record MedicationPrescriptionInformation(
        Long prescriptionId,
        Long medicationId,
        String medicationName,
        Integer quantityPrescribed,
        String dosageInstructions,
        String duration
) {}
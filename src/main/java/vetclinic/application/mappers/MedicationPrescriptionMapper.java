package vetclinic.application.mappers;

import vetclinic.application.outputDTO.MedicationPrescriptionInformation;
import vetclinic.domain.MedicationPrescription;

public class MedicationPrescriptionMapper {

    public static MedicationPrescriptionInformation toInformation(MedicationPrescription prescription) {
        return new MedicationPrescriptionInformation(
                prescription.getPrescriptionId(),
                prescription.getMedication().getMedicationId(),
                prescription.getMedication().getName(),
                prescription.getQuantityPrescribed(),
                prescription.getDosageInstructions(),
                prescription.getDuration()
        );
    }
}
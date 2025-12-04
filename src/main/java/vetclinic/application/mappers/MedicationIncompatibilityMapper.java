package vetclinic.application.mappers;

import vetclinic.application.outputDTO.MedicationIncompatibilityDTO;
import vetclinic.domain.MedicationIncompatibility;

public class MedicationIncompatibilityMapper {

    public static MedicationIncompatibilityDTO toDTO(MedicationIncompatibility incompatibility) {
        return new MedicationIncompatibilityDTO(
                incompatibility.getIncompatibilityId(),
                incompatibility.getMedication1().getMedicationId(),
                incompatibility.getMedication1().getName(),
                incompatibility.getMedication2().getMedicationId(),
                incompatibility.getMedication2().getName(),
                incompatibility.getPersistingPeriodDays()
        );
    }
}
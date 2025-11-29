package vetclinic.application.mappers;

import vetclinic.application.outputDTO.TreatmentInformation;
import vetclinic.domain.Treatment;

public class TreatmentMapper {

    public static TreatmentInformation toInformation(Treatment treatment) {
        return new TreatmentInformation(
                treatment.getTreatmentId(),
                treatment.getName(),
                treatment.getDescription(),
                treatment.getCost()
        );
    }
}
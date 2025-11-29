package vetclinic.application.mappers;

import vetclinic.application.outputDTO.VisitInformation;
import vetclinic.domain.Visit;
import java.util.stream.Collectors;

public class VisitMapper {

    public static VisitInformation toInformation(Visit visit) {
        return new VisitInformation(
                visit.getVisitId(),
                visit.getPet().getPetId(),
                visit.getPet().getName(),
                visit.getPetOwner().getPersonId(),
                visit.getPetOwner().getFirstName() + " " + visit.getPetOwner().getLastName(),
                visit.getVeterinarian().getPersonId(),
                visit.getVeterinarian().getFirstName() + " " + visit.getVeterinarian().getLastName(),
                visit.getDateTime(),
                visit.getDuration(),
                visit.getReasonForVisit(),
                visit.getStatus().name(),
                visit.getDiagnosis(),
                visit.getNotes(),
                visit.calculateTotalCost(),
                visit.getTreatments().stream()
                        .map(TreatmentMapper::toInformation)
                        .collect(Collectors.toList()),
                visit.getMedicationPrescriptions().stream()
                        .map(MedicationPrescriptionMapper::toInformation)
                        .collect(Collectors.toList())
        );
    }
}
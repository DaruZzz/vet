package vetclinic.application;

import vetclinic.application.exceptions.PetNotFoundException;
import vetclinic.application.outputDTO.*;
import vetclinic.domain.*;
import vetclinic.persistence.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    // UC 2.5: View Pet's Medical History
    @Transactional(readOnly = true)
    public PetMedicalHistoryDTO getPetMedicalHistory(Long petId) {
        Pet pet = petRepository.findByIdWithMedicalHistory(petId)
                .orElseThrow(() -> new PetNotFoundException(
                        "Pet with id " + petId + " not found"
                ));

        List<MedicalEventDTO> medicalHistory = pet.getVisits().stream()
                .sorted((v1, v2) -> v2.getDateTime().compareTo(v1.getDateTime())) // Most recent first
                .map(this::mapToMedicalEvent)
                .collect(Collectors.toList());

        return new PetMedicalHistoryDTO(
                pet.getPetId(),
                pet.getName(),
                pet.getPetType().getName(),
                pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName(),
                medicalHistory
        );
    }

    private MedicalEventDTO mapToMedicalEvent(Visit visit) {
        List<String> treatments = visit.getTreatments().stream()
                .map(t -> t.getName() + " - " + t.getDescription())
                .collect(Collectors.toList());

        List<String> medications = visit.getMedicationPrescriptions().stream()
                .map(p -> p.getMedication().getName() +
                        " (" + p.getQuantityPrescribed() + " " +
                        p.getMedication().getDosageUnit() + ") - " +
                        p.getDosageInstructions())
                .collect(Collectors.toList());

        return new MedicalEventDTO(
                visit.getVisitId(),
                visit.getDateTime(),
                visit.getVeterinarian().getFirstName() + " " + visit.getVeterinarian().getLastName(),
                visit.getReasonForVisit(),
                visit.getDiagnosis(),
                visit.getNotes(),
                visit.getStatus().name(),
                treatments,
                medications
        );
    }
}
package vetclinic.application;

import vetclinic.application.exceptions.MedicationIncompatibilityNotFoundException;
import vetclinic.application.exceptions.MedicationNotFoundException;
import vetclinic.application.inputDTO.CreateIncompatibilityCommand;
import vetclinic.application.inputDTO.UpdateIncompatibilityCommand;
import vetclinic.application.mappers.MedicationIncompatibilityMapper;
import vetclinic.application.outputDTO.IncompatibilityAlertDTO;
import vetclinic.application.outputDTO.MedicationIncompatibilityDTO;
import vetclinic.domain.Medication;
import vetclinic.domain.MedicationIncompatibility;
import vetclinic.domain.MedicationPrescription;
import vetclinic.domain.Pet;
import vetclinic.persistence.MedicationIncompatibilityRepository;
import vetclinic.persistence.MedicationRepository;
import vetclinic.persistence.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationIncompatibilityService {

    private final MedicationIncompatibilityRepository incompatibilityRepository;
    private final MedicationRepository medicationRepository;
    private final PetRepository petRepository;

    public MedicationIncompatibilityService(
            MedicationIncompatibilityRepository incompatibilityRepository,
            MedicationRepository medicationRepository,
            PetRepository petRepository) {
        this.incompatibilityRepository = incompatibilityRepository;
        this.medicationRepository = medicationRepository;
        this.petRepository = petRepository;
    }

    // UC 2.11: Create Incompatibility
    @Transactional
    public Long createIncompatibility(CreateIncompatibilityCommand command) {
        // Validar que las medicaciones existan
        Medication med1 = medicationRepository.findById(command.medication1Id())
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + command.medication1Id() + " not found"
                ));

        Medication med2 = medicationRepository.findById(command.medication2Id())
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + command.medication2Id() + " not found"
                ));

        // Validar que no sean la misma medicación
        if (command.medication1Id().equals(command.medication2Id())) {
            throw new IllegalArgumentException("Cannot create incompatibility between same medication");
        }

        // Verificar que no exista ya esta incompatibilidad
        incompatibilityRepository.findByMedicationPair(
                command.medication1Id(),
                command.medication2Id()
        ).ifPresent(existing -> {
            throw new IllegalStateException(
                    "Incompatibility already exists between these medications"
            );
        });

        // Crear la incompatibilidad
        MedicationIncompatibility incompatibility = new MedicationIncompatibility();
        incompatibility.setMedication1(med1);
        incompatibility.setMedication2(med2);
        incompatibility.setPersistingPeriodDays(command.persistingPeriodDays());

        MedicationIncompatibility saved = incompatibilityRepository.save(incompatibility);
        return saved.getIncompatibilityId();
    }

    // UC 2.11: Update Incompatibility
    @Transactional
    public void updateIncompatibility(Long incompatibilityId, UpdateIncompatibilityCommand command) {
        MedicationIncompatibility incompatibility = incompatibilityRepository
                .findById(incompatibilityId)
                .orElseThrow(() -> new MedicationIncompatibilityNotFoundException(
                        "Incompatibility with id " + incompatibilityId + " not found"
                ));

        incompatibility.setPersistingPeriodDays(command.persistingPeriodDays());
        incompatibilityRepository.save(incompatibility);
    }

    // UC 2.11: Delete Incompatibility
    @Transactional
    public void deleteIncompatibility(Long incompatibilityId) {
        MedicationIncompatibility incompatibility = incompatibilityRepository
                .findById(incompatibilityId)
                .orElseThrow(() -> new MedicationIncompatibilityNotFoundException(
                        "Incompatibility with id " + incompatibilityId + " not found"
                ));

        incompatibilityRepository.delete(incompatibility);
    }

    // UC 2.11: Get all incompatibilities for a medication
    @Transactional(readOnly = true)
    public List<MedicationIncompatibilityDTO> getIncompatibilitiesForMedication(Long medicationId) {
        // Verificar que la medicación existe
        medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + medicationId + " not found"
                ));

        List<MedicationIncompatibility> incompatibilities =
                incompatibilityRepository.findByMedicationId(medicationId);

        return incompatibilities.stream()
                .map(MedicationIncompatibilityMapper::toDTO)
                .collect(Collectors.toList());
    }

    // UC 2.12: Check if medication has incompatibilities with pet's current/recent medications
    @Transactional(readOnly = true)
    public boolean checkIncompatibility(Long petId, Long medicationId) {
        List<IncompatibilityAlertDTO> alerts = getIncompatibilityAlerts(petId, medicationId);
        return !alerts.isEmpty();
    }

    // UC 2.12: Get detailed incompatibility alerts
    @Transactional(readOnly = true)
    public List<IncompatibilityAlertDTO> getIncompatibilityAlerts(Long petId, Long medicationId) {
        // Obtener el pet con su historial médico
        Pet pet = petRepository.findByIdWithMedicalHistory(petId)
                .orElseThrow(() -> new vetclinic.application.exceptions.PetNotFoundException(
                        "Pet with id " + petId + " not found"
                ));

        // Verificar que la medicación existe
        Medication targetMedication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + medicationId + " not found"
                ));

        // Obtener todas las incompatibilidades de la medicación objetivo
        List<MedicationIncompatibility> incompatibilities =
                incompatibilityRepository.findByMedicationId(medicationId);

        if (incompatibilities.isEmpty()) {
            return new ArrayList<>();
        }

        // Obtener todas las prescripciones del pet
        List<MedicationPrescription> allPrescriptions = pet.getVisits().stream()
                .flatMap(visit -> visit.getMedicationPrescriptions().stream())
                .collect(Collectors.toList());

        List<IncompatibilityAlertDTO> alerts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Para cada prescripción del pet
        for (MedicationPrescription prescription : allPrescriptions) {
            Long prescribedMedicationId = prescription.getMedication().getMedicationId();

            // Buscar si hay incompatibilidad con esta medicación
            for (MedicationIncompatibility incomp : incompatibilities) {
                Medication otherMedication = null;

                if (incomp.getMedication1().getMedicationId().equals(prescribedMedicationId)) {
                    otherMedication = incomp.getMedication1();
                } else if (incomp.getMedication2().getMedicationId().equals(prescribedMedicationId)) {
                    otherMedication = incomp.getMedication2();
                }

                if (otherMedication != null) {
                    // Calcular si la incompatibilidad está activa
                    LocalDateTime prescriptionDate = prescription.getVisit().getDateTime();
                    boolean isActive = true;
                    Integer daysRemaining = null;

                    if (incomp.getPersistingPeriodDays() != null) {
                        long daysSincePrescription = ChronoUnit.DAYS.between(prescriptionDate, now);
                        daysRemaining = incomp.getPersistingPeriodDays() - (int) daysSincePrescription;
                        isActive = daysRemaining > 0;
                    }

                    if (isActive) {
                        String reason = incomp.getPersistingPeriodDays() == null
                                ? "Always incompatible"
                                : "Incompatible for " + incomp.getPersistingPeriodDays() + " days after last dose";

                        alerts.add(new IncompatibilityAlertDTO(
                                otherMedication.getName(),
                                reason,
                                prescriptionDate,
                                daysRemaining,
                                true
                        ));
                    }
                }
            }
        }

        return alerts;
    }

    // Get all incompatibilities (for admin purposes)
    @Transactional(readOnly = true)
    public List<MedicationIncompatibilityDTO> getAllIncompatibilities() {
        return ((List<MedicationIncompatibility>) incompatibilityRepository.findAll())
                .stream()
                .map(MedicationIncompatibilityMapper::toDTO)
                .collect(Collectors.toList());
    }
}
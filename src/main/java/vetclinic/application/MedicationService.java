package vetclinic.application;

import vetclinic.application.exceptions.MedicationNotFoundException;
import vetclinic.application.inputDTO.MedicationBatchCommand;
import vetclinic.application.mappers.MedicationBatchMapper;
import vetclinic.application.outputDTO.LowStockMedicationDTO;
import vetclinic.application.outputDTO.MedicationBatchInformation;
import vetclinic.domain.Medication;
import vetclinic.domain.MedicationBatch;
import vetclinic.persistence.MedicationBatchRepository;
import vetclinic.persistence.MedicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationBatchRepository medicationBatchRepository;

    public MedicationService(MedicationRepository medicationRepository,
                             MedicationBatchRepository medicationBatchRepository) {
        this.medicationRepository = medicationRepository;
        this.medicationBatchRepository = medicationBatchRepository;
    }

    // UC 2.9: Receive New Medication Batch
    @Transactional
    public Long addMedicationBatch(MedicationBatchCommand command) {
        Medication medication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new MedicationNotFoundException(
                        "Medication with id " + command.medicationId() + " not found"
                ));

        MedicationBatch batch = MedicationBatchMapper.commandToDomain(command);

        // Validate batch before adding
        batch.validateBatch();

        medication.addBatch(batch);

        MedicationBatch saved = medicationBatchRepository.save(batch);
        return saved.getBatchId();
    }

    public MedicationBatchInformation getMedicationBatch(Long batchId) {
        MedicationBatch batch = medicationBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        return MedicationBatchMapper.toInformation(batch);
    }

    // UC 2.10: Receive Low Stock Alert
    public List<LowStockMedicationDTO> getLowStockMedications() {
        return medicationRepository.findLowStockMedications();
    }
}
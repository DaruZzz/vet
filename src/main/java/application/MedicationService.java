package application;

import application.exceptions.MedicationNotFoundException;
import application.inputDTO.MedicationBatchCommand;
import application.mappers.MedicationBatchMapper;
import application.outputDTO.LowStockMedicationDTO;
import application.outputDTO.MedicationBatchInformation;
import domain.Medication;
import domain.MedicationBatch;
import persistence.MedicationBatchRepository;
import persistence.MedicationRepository;
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
package api;

import application.MedicationService;
import application.inputDTO.MedicationBatchCommand;
import application.outputDTO.LowStockMedicationDTO;
import application.outputDTO.MedicationBatchInformation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationRestController {

    private final MedicationService medicationService;

    public MedicationRestController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // UC 2.9: Add Medication Batch
    @PostMapping("/batches")
    public ResponseEntity<Void> addMedicationBatch(
            @RequestBody @Valid MedicationBatchCommand command,
            UriComponentsBuilder uriBuilder) {

        Long batchId = medicationService.addMedicationBatch(command);
        var location = uriBuilder
                .path("/api/medications/batches/{id}")
                .buildAndExpand(batchId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/batches/{batchId}")
    public MedicationBatchInformation getMedicationBatch(@PathVariable Long batchId) {
        return medicationService.getMedicationBatch(batchId);
    }

    // UC 2.10: Receive Low Stock Alert
    @GetMapping("/low-stock")
    public List<LowStockMedicationDTO> getLowStockMedications() {
        return medicationService.getLowStockMedications();
    }
}
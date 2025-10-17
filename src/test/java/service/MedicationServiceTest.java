package service;

import application.MedicationService;
import application.inputDTO.MedicationBatchCommand;
import application.outputDTO.LowStockMedicationDTO;
import application.outputDTO.MedicationBatchInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = {
        "DELETE FROM medication_batch",
        "DELETE FROM medication",
        "ALTER TABLE medication ALTER COLUMN medication_id RESTART WITH 1"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MedicationServiceTest {

    @Autowired
    private MedicationService medicationService;

    @Test
    public void testAddMedicationBatch() {
        MedicationBatchCommand command = new MedicationBatchCommand(
                1L,
                "TEST-2024-001",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                100,
                10.0,
                "Shelf A1"
        );

        Long batchId = medicationService.addMedicationBatch(command);

        assertNotNull(batchId);

        MedicationBatchInformation batch = medicationService.getMedicationBatch(batchId);
        assertNotNull(batch);
        assertEquals("TEST-2024-001", batch.lotNumber());
    }

    @Test
    public void testGetLowStockMedications() {
        List<LowStockMedicationDTO> lowStockMeds = medicationService.getLowStockMedications();

        assertNotNull(lowStockMeds);
        // Should have some low stock medications based on data.sql
    }
}
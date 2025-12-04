package vetclinic.service;

import vetclinic.application.MedicationService;
import vetclinic.application.inputDTO.MedicationBatchCommand;
import vetclinic.application.outputDTO.LowStockMedicationDTO;
import vetclinic.application.outputDTO.MedicationBatchInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MedicationServiceTest {

    @Autowired
    private MedicationService medicationService;

    @Test
    public void testAddMedicationBatch() {
        MedicationBatchCommand command = new MedicationBatchCommand(
                1L,
                "TEST-2025-001",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                100,
                1.50,
                "Shelf A1"
        );

        Long batchId = medicationService.addMedicationBatch(command);

        assertNotNull(batchId);

        MedicationBatchInformation batch = medicationService.getMedicationBatch(batchId);
        assertNotNull(batch);
        assertEquals("TEST-2025-001", batch.lotNumber());
        assertEquals(100, batch.currentQuantity());
    }

    @Test
    public void testGetLowStockMedications() {
        List<LowStockMedicationDTO> lowStockMeds = medicationService.getLowStockMedications();

        assertNotNull(lowStockMeds);

        // Check that medications with low stock are returned
        // From data-test.sql:
        // - Test Med 1: current 40, threshold 50 (LOW STOCK)
        // - Test Med 2: current 15, threshold 20 (LOW STOCK)
        // - Test Med 3: current 25, threshold 30 (LOW STOCK)
        assertTrue(lowStockMeds.size() >= 3,
                "Should have at least 3 low stock medications");
    }

    @Test
    public void testAddExpiredBatch() {
        MedicationBatchCommand command = new MedicationBatchCommand(
                1L,
                "EXPIRED-BATCH",
                LocalDate.now().minusYears(3),
                LocalDate.now().minusYears(1), // Already expired
                50,
                1.00,
                "Shelf B"
        );

        assertThrows(RuntimeException.class, () ->
                medicationService.addMedicationBatch(command)
        );
    }

    @Test
    public void testAddBatchWithInvalidQuantity() {
        MedicationBatchCommand command = new MedicationBatchCommand(
                1L,
                "INVALID-BATCH",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                -10, // Invalid quantity
                1.00,
                "Shelf C"
        );

        assertThrows(RuntimeException.class, () ->
                medicationService.addMedicationBatch(command)
        );
    }
}
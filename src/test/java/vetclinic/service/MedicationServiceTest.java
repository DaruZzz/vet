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
public class MedicationServiceTest {

    @Autowired
    private MedicationService medicationService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testAddMedicationBatch() {
        MedicationBatchCommand command = new MedicationBatchCommand(
                1L, // Este medication ID existe en data-test.sql
                "TEST-2024-003",
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
        assertEquals("TEST-2024-003", batch.lotNumber());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetLowStockMedications() {
        List<LowStockMedicationDTO> lowStockMeds = medicationService.getLowStockMedications();

        assertNotNull(lowStockMeds);
        // Los medicamentos en data-test.sql tienen stock bajo
        assertTrue(lowStockMeds.size() >= 1);
    }
}
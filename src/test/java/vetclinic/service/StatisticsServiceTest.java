package vetclinic.service;

import vetclinic.application.StatisticsService;
import vetclinic.application.outputDTO.*;
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
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testGetSpecialitiesByDemand() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<SpecialityDemandDTO> result =
                statisticsService.getSpecialitiesByDemand(startDate, endDate);

        assertNotNull(result);
        // Should have specialities from scheduled visits
        assertFalse(result.isEmpty());

        // Check that results are ordered by demand (descending)
        if (result.size() > 1) {
            for (int i = 0; i < result.size() - 1; i++) {
                assertTrue(
                        result.get(i).visitCount() >= result.get(i + 1).visitCount(),
                        "Results should be ordered by visit count descending"
                );
            }
        }
    }

    @Test
    public void testGetVeterinariansByDemand() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<VeterinarianDemandDTO> result =
                statisticsService.getVeterinariansByDemand(startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify veterinarians have scheduled visits
        result.forEach(vet -> {
            assertTrue(vet.visitCount() > 0);
        });
    }

    @Test
    public void testGetMedicationsByPrescription() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<MedicationPrescriptionStatsDTO> result =
                statisticsService.getMedicationsByPrescription(startDate, endDate);

        assertNotNull(result);
        // From data-test.sql, we have 3 prescriptions
        assertEquals(3, result.size());

        // Check ordering
        if (result.size() > 1) {
            for (int i = 0; i < result.size() - 1; i++) {
                assertTrue(
                        result.get(i).prescriptionCount() >= result.get(i + 1).prescriptionCount()
                );
            }
        }
    }

    @Test
    public void testGetVeterinariansByMedicationPrescription() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // Query for Test Med 1 (medication ID 1)
        List<VeterinarianPrescriptionStatsDTO> result =
                statisticsService.getVeterinariansByMedicationPrescription(
                        1L, startDate, endDate
                );

        assertNotNull(result);
        // Test Med 1 is prescribed by Vet 1 in data-test.sql
        assertFalse(result.isEmpty());

        result.forEach(vet -> {
            assertTrue(vet.prescriptionCount() > 0);
        });
    }

    @Test
    public void testEmptyPeriod() {
        // Query for a period with no data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        List<SpecialityDemandDTO> result =
                statisticsService.getSpecialitiesByDemand(startDate, endDate);

        assertNotNull(result);
        // Should return empty list for period with no visits
        assertTrue(result.isEmpty());
    }
}
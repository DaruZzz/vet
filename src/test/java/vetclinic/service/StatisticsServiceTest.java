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
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetSpecialitiesByDemand() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<SpecialityDemandDTO> result =
                statisticsService.getSpecialitiesByDemand(startDate, endDate);

        assertNotNull(result);
        // Should have at least some specialities with scheduled visits
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetVeterinariansByDemand() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<VeterinarianDemandDTO> result =
                statisticsService.getVeterinariansByDemand(startDate, endDate);

        assertNotNull(result);
        // Should have veterinarians with scheduled visits
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetMedicationsByPrescription() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<MedicationPrescriptionStatsDTO> result =
                statisticsService.getMedicationsByPrescription(startDate, endDate);

        assertNotNull(result);
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetVeterinariansByMedicationPrescription() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<VeterinarianPrescriptionStatsDTO> result =
                statisticsService.getVeterinariansByMedicationPrescription(
                        1L, startDate, endDate
                );

        assertNotNull(result);
    }
}
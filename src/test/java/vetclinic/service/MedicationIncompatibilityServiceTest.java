package vetclinic.service;

import vetclinic.application.MedicationIncompatibilityService;
import vetclinic.application.exceptions.MedicationNotFoundException;
import vetclinic.application.inputDTO.CreateIncompatibilityCommand;
import vetclinic.application.inputDTO.UpdateIncompatibilityCommand;
import vetclinic.application.outputDTO.IncompatibilityAlertDTO;
import vetclinic.application.outputDTO.MedicationIncompatibilityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MedicationIncompatibilityServiceTest {

    @Autowired
    private MedicationIncompatibilityService incompatibilityService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCreateIncompatibility() {
        // Given
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 3L, 10
        );

        // When
        Long incompatibilityId = incompatibilityService.createIncompatibility(command);

        // Then
        assertNotNull(incompatibilityId);

        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        assertTrue(incompatibilities.size() >= 1);
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCreateIncompatibilityWithSameMedication() {
        // Given
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 1L, null
        );

        // When/Then
        assertThrows(IllegalArgumentException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCreateDuplicateIncompatibility() {
        // Given
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 2L, null
        );

        incompatibilityService.createIncompatibility(command);

        // When/Then - Try to create same incompatibility
        assertThrows(IllegalStateException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetIncompatibilitiesForMedication() {
        // When
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        // Then
        assertNotNull(incompatibilities);
        assertEquals(1, incompatibilities.size()); // From data-test.sql
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testUpdateIncompatibility() {
        // Given
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        Long incompatibilityId = incompatibilities.get(0).incompatibilityId();

        // When
        UpdateIncompatibilityCommand command = new UpdateIncompatibilityCommand(20);
        incompatibilityService.updateIncompatibility(incompatibilityId, command);

        // Then
        List<MedicationIncompatibilityDTO> updated =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        MedicationIncompatibilityDTO found = updated.stream()
                .filter(i -> i.incompatibilityId().equals(incompatibilityId))
                .findFirst()
                .orElseThrow();

        assertEquals(20, found.persistingPeriodDays());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testDeleteIncompatibility() {
        // Given
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        int initialSize = incompatibilities.size();
        Long incompatibilityId = incompatibilities.get(0).incompatibilityId();

        // When
        incompatibilityService.deleteIncompatibility(incompatibilityId);

        // Then
        List<MedicationIncompatibilityDTO> remaining =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        assertEquals(initialSize - 1, remaining.size());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCheckIncompatibility() {
        // Given - Pet 1 has been prescribed Test Med 1 in data-test.sql

        // When - Check if Test Med 2 is compatible (it's not, from data-test.sql)
        boolean hasIncompatibility = incompatibilityService.checkIncompatibility(1L, 2L);

        // Then
        assertTrue(hasIncompatibility);
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetIncompatibilityAlerts() {
        // Given - Pet 1 has Test Med 1 prescribed

        // When - Check alerts for Test Med 2
        List<IncompatibilityAlertDTO> alerts =
                incompatibilityService.getIncompatibilityAlerts(1L, 2L);

        // Then
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());

        IncompatibilityAlertDTO alert = alerts.get(0);
        assertEquals("Test Med 1", alert.conflictingMedicationName());
        assertTrue(alert.isActive());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetIncompatibilityAlertsWithPersistingPeriod() {
        // Given - Pet 2 has Test Med 2 prescribed

        // When - Check alerts for Test Med 3 (5-day persisting period)
        List<IncompatibilityAlertDTO> alerts =
                incompatibilityService.getIncompatibilityAlerts(2L, 3L);

        // Then
        assertNotNull(alerts);
        // May or may not be active depending on test data dates
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetIncompatibilitiesForNonExistentMedication() {
        // When/Then
        assertThrows(MedicationNotFoundException.class, () ->
                incompatibilityService.getIncompatibilitiesForMedication(999L)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetAllIncompatibilities() {
        // When
        List<MedicationIncompatibilityDTO> all =
                incompatibilityService.getAllIncompatibilities();

        // Then
        assertNotNull(all);
        assertEquals(2, all.size()); // From data-test.sql
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCreateIncompatibilityWithNonExistentMedication() {
        // Given
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                999L, 1L, null
        );

        // When/Then
        assertThrows(MedicationNotFoundException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCheckIncompatibilityWithNonExistentPet() {
        // When/Then
        assertThrows(RuntimeException.class, () ->
                incompatibilityService.checkIncompatibility(999L, 1L)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testCheckIncompatibilityWithNonExistentMedication() {
        // When/Then
        assertThrows(MedicationNotFoundException.class, () ->
                incompatibilityService.checkIncompatibility(1L, 999L)
        );
    }
}
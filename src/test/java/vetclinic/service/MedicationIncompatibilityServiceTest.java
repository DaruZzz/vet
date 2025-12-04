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
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MedicationIncompatibilityServiceTest {

    @Autowired
    private MedicationIncompatibilityService incompatibilityService;

    @Test
    public void testCreateIncompatibility() {
        // Create a new incompatibility between medications 1 and 3
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 3L, 10
        );

        Long incompatibilityId = incompatibilityService.createIncompatibility(command);

        assertNotNull(incompatibilityId);

        // Verify it was created
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        assertTrue(incompatibilities.stream()
                .anyMatch(i -> i.medication2Id().equals(3L)));
    }

    @Test
    public void testCreateIncompatibilityWithSameMedication() {
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 1L, null
        );

        assertThrows(IllegalArgumentException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    public void testCreateDuplicateIncompatibility() {
        // First incompatibility already exists in data-test.sql between 1 and 2
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                1L, 2L, null
        );

        assertThrows(IllegalStateException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    public void testGetIncompatibilitiesForMedication() {
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        assertNotNull(incompatibilities);
        assertEquals(1, incompatibilities.size());
    }

    @Test
    public void testUpdateIncompatibility() {
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        assertFalse(incompatibilities.isEmpty(), "Should have at least one incompatibility");

        Long incompatibilityId = incompatibilities.get(0).incompatibilityId();

        UpdateIncompatibilityCommand command = new UpdateIncompatibilityCommand(20);
        incompatibilityService.updateIncompatibility(incompatibilityId, command);

        List<MedicationIncompatibilityDTO> updated =
                incompatibilityService.getIncompatibilitiesForMedication(1L);

        MedicationIncompatibilityDTO found = updated.stream()
                .filter(i -> i.incompatibilityId().equals(incompatibilityId))
                .findFirst()
                .orElseThrow();

        assertEquals(20, found.persistingPeriodDays());
    }

    @Test
    public void testDeleteIncompatibility() {
        List<MedicationIncompatibilityDTO> incompatibilities =
                incompatibilityService.getIncompatibilitiesForMedication(2L);

        assertFalse(incompatibilities.isEmpty());

        Long incompatibilityId = incompatibilities.get(0).incompatibilityId();

        incompatibilityService.deleteIncompatibility(incompatibilityId);

        List<MedicationIncompatibilityDTO> remaining =
                incompatibilityService.getIncompatibilitiesForMedication(2L);

        assertTrue(remaining.stream()
                .noneMatch(i -> i.incompatibilityId().equals(incompatibilityId)));
    }

    @Test
    public void testCheckIncompatibility() {
        // Pet 1 has been prescribed Test Med 1 in data-test.sql
        // Check if Test Med 2 is compatible (it's not, from data-test.sql)
        boolean hasIncompatibility = incompatibilityService.checkIncompatibility(1L, 2L);

        assertTrue(hasIncompatibility, "Pet 1 with Med 1 should be incompatible with Med 2");
    }

    @Test
    public void testGetIncompatibilityAlerts() {
        // Pet 1 has Test Med 1 prescribed
        List<IncompatibilityAlertDTO> alerts =
                incompatibilityService.getIncompatibilityAlerts(1L, 2L);

        assertNotNull(alerts);
        assertFalse(alerts.isEmpty(), "Should have incompatibility alerts");

        IncompatibilityAlertDTO alert = alerts.get(0);
        assertEquals("Test Med 1", alert.conflictingMedicationName());
        assertTrue(alert.isActive());
    }

    @Test
    public void testGetAllIncompatibilities() {
        List<MedicationIncompatibilityDTO> all =
                incompatibilityService.getAllIncompatibilities();

        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    public void testCreateIncompatibilityWithNonExistentMedication() {
        CreateIncompatibilityCommand command = new CreateIncompatibilityCommand(
                999L, 1L, null
        );

        assertThrows(MedicationNotFoundException.class, () ->
                incompatibilityService.createIncompatibility(command)
        );
    }

    @Test
    public void testGetIncompatibilitiesForNonExistentMedication() {
        assertThrows(MedicationNotFoundException.class, () ->
                incompatibilityService.getIncompatibilitiesForMedication(999L)
        );
    }
}
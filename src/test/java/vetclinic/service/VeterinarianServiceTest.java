package vetclinic.service;

import vetclinic.application.VeterinarianService;
import vetclinic.application.exceptions.VeterinarianNotFoundException;
import vetclinic.application.inputDTO.AvailabilityCommand;
import vetclinic.application.inputDTO.AvailabilityExceptionCommand;
import vetclinic.application.outputDTO.AvailabilityInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VeterinarianServiceTest {

    @Autowired
    private VeterinarianService veterinarianService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testAddAvailability() {
        // Given: A new availability command for Saturday
        AvailabilityCommand command = new AvailabilityCommand(
                DayOfWeek.SATURDAY,
                LocalTime.of(10, 0),
                LocalTime.of(15, 0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        // When: Adding the availability
        Long availabilityId = veterinarianService.addAvailability(1L, command);

        // Then: Availability should be created
        assertNotNull(availabilityId, "Availability ID should not be null");

        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);

        assertTrue(availabilities.size() >= 3,
                "Should have at least 3 availabilities (2 from data-test.sql + 1 new)");
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetVeterinarianAvailabilities() {
        // When: Retrieving veterinarian availabilities
        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);

        // Then: Should return 2 availabilities from data-test.sql
        assertNotNull(availabilities, "Availabilities list should not be null");
        assertEquals(2, availabilities.size(),
                "Veterinarian 1 should have exactly 2 availabilities from data-test.sql");

        // Verify days
        boolean hasMonday = availabilities.stream()
                .anyMatch(a -> a.dayOfWeek().equals(DayOfWeek.MONDAY));
        boolean hasTuesday = availabilities.stream()
                .anyMatch(a -> a.dayOfWeek().equals(DayOfWeek.TUESDAY));

        assertTrue(hasMonday, "Should have Monday availability");
        assertTrue(hasTuesday, "Should have Tuesday availability");
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testUpdateAvailability() {
        // Given: Get first availability
        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);
        Long availabilityId = availabilities.get(0).availabilityId();

        // When: Update the availability
        AvailabilityCommand updateCommand = new AvailabilityCommand(
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        veterinarianService.updateAvailability(availabilityId, updateCommand);

        // Then: Verify update
        List<AvailabilityInformation> updated =
                veterinarianService.getVeterinarianAvailabilities(1L);

        AvailabilityInformation found = updated.stream()
                .filter(a -> a.availabilityId().equals(availabilityId))
                .findFirst()
                .orElseThrow();

        assertEquals(LocalTime.of(8, 0), found.startTime());
        assertEquals(LocalTime.of(13, 0), found.endTime());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testDeleteAvailability() {
        // Given
        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);
        int initialSize = availabilities.size();
        Long availabilityId = availabilities.get(0).availabilityId();

        // When
        veterinarianService.deleteAvailability(1L, availabilityId);

        // Then
        List<AvailabilityInformation> remaining =
                veterinarianService.getVeterinarianAvailabilities(1L);

        assertEquals(initialSize - 1, remaining.size());

        boolean stillExists = remaining.stream()
                .anyMatch(a -> a.availabilityId().equals(availabilityId));

        assertFalse(stillExists, "Deleted availability should not exist");
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testAddAvailabilityException() {
        // Given
        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);
        Long availabilityId = availabilities.get(0).availabilityId();

        // When
        AvailabilityExceptionCommand exceptionCommand = new AvailabilityExceptionCommand(
                LocalDate.of(2024, 12, 25),
                null,
                null,
                "Christmas Day"
        );

        // Then: Should not throw exception
        assertDoesNotThrow(() ->
                veterinarianService.addAvailabilityException(availabilityId, exceptionCommand)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetAvailabilitiesForNonExistentVeterinarian() {
        // When/Then
        assertThrows(VeterinarianNotFoundException.class, () ->
                veterinarianService.getVeterinarianAvailabilities(999L)
        );
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testAddAvailabilityToNonExistentVeterinarian() {
        // Given
        AvailabilityCommand command = new AvailabilityCommand(
                DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        // When/Then
        assertThrows(VeterinarianNotFoundException.class, () ->
                veterinarianService.addAvailability(999L, command)
        );
    }
}
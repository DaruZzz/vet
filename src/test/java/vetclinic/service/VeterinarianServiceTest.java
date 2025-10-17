package vetclinic.service;

import vetclinic.application.VeterinarianService;
import vetclinic.application.inputDTO.AvailabilityCommand;
import vetclinic.application.outputDTO.AvailabilityInformation;
import vetclinic.persistence.VeterinarianRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = {
        "DELETE FROM availability_exception",
        "DELETE FROM availability",
        "DELETE FROM veterinarian_speciality",
        "DELETE FROM veterinarian",
        "DELETE FROM person",
        "ALTER TABLE person ALTER COLUMN person_id RESTART WITH 1"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VeterinarianServiceTest {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Test
    public void testAddAvailability() {
        AvailabilityCommand command = new AvailabilityCommand(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        Long availabilityId = veterinarianService.addAvailability(1L, command);

        assertNotNull(availabilityId);

        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);

        assertTrue(availabilities.size() > 0);
    }

    @Test
    public void testGetVeterinarianAvailabilities() {
        List<AvailabilityInformation> availabilities =
                veterinarianService.getVeterinarianAvailabilities(1L);

        assertNotNull(availabilities);
        assertTrue(availabilities.size() >= 0);
    }
}
package vetclinic.service;

import vetclinic.application.PetService;
import vetclinic.application.exceptions.PetNotFoundException;
import vetclinic.application.outputDTO.PetMedicalHistoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Test
    public void testGetPetMedicalHistory() {
        PetMedicalHistoryDTO history = petService.getPetMedicalHistory(1L);

        assertNotNull(history);
        assertEquals(1L, history.petId());
        assertEquals("TestDog", history.petName());
        assertNotNull(history.medicalHistory());

        // From data-test.sql, TestDog should have 2 completed visits
        assertTrue(history.medicalHistory().size() >= 2);
    }

    @Test
    public void testGetPetMedicalHistoryNotFound() {
        assertThrows(PetNotFoundException.class, () ->
                petService.getPetMedicalHistory(999L)
        );
    }

    @Test
    public void testMedicalHistoryIsSorted() {
        PetMedicalHistoryDTO history = petService.getPetMedicalHistory(1L);

        assertNotNull(history);

        if (history.medicalHistory().size() > 1) {
            for (int i = 0; i < history.medicalHistory().size() - 1; i++) {
                var current = history.medicalHistory().get(i);
                var next = history.medicalHistory().get(i + 1);

                // Most recent should be first
                assertTrue(
                        current.dateTime().isAfter(next.dateTime()) ||
                                current.dateTime().isEqual(next.dateTime()),
                        "Medical history should be sorted with most recent first"
                );
            }
        }
    }
}
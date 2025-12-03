package vetclinic.service;

import vetclinic.application.PetOwnerService;
import vetclinic.application.exceptions.PetOwnerNotFoundException;
import vetclinic.application.outputDTO.FidelityPointsBalanceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PetOwnerServiceTest {

    @Autowired
    private PetOwnerService petOwnerService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetFidelityPointsBalance() {
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(100L);

        assertNotNull(balance);
        assertEquals(100L, balance.petOwnerId());
        assertNotNull(balance.currentPoints());
        assertNotNull(balance.currentTier());
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetFidelityPointsBalanceNotFound() {
        assertThrows(PetOwnerNotFoundException.class, () -> {
            petOwnerService.getFidelityPointsBalance(999L);
        });
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testFidelityPointsBalanceWithNextTier() {
        // Pet owner 100 has 50 points (Bronze tier)
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(100L);

        assertNotNull(balance);
        assertEquals("Bronze", balance.currentTier());
        assertEquals("Silver", balance.nextTier());
        assertNotNull(balance.pointsToNextTier());
        assertTrue(balance.pointsToNextTier() > 0);
    }

    @Test
    @Sql(scripts = "classpath:data-test.sql")
    public void testFidelityPointsBalanceHighTier() {
        // Pet owner 101 has 150 points (Silver tier)
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(101L);

        assertNotNull(balance);
        assertEquals("Silver", balance.currentTier());
        assertEquals("Gold", balance.nextTier());
    }
}
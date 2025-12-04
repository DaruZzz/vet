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
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PetOwnerServiceTest {

    @Autowired
    private PetOwnerService petOwnerService;

    @Test
    public void testGetFidelityPointsBalance() {
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(100L);

        assertNotNull(balance);
        assertEquals(100L, balance.petOwnerId());
        assertEquals(50, balance.currentPoints());
        assertEquals("Bronze", balance.currentTier());
    }

    @Test
    public void testGetFidelityPointsBalanceNotFound() {
        assertThrows(PetOwnerNotFoundException.class, () ->
                petOwnerService.getFidelityPointsBalance(999L)
        );
    }

    @Test
    public void testFidelityPointsBalanceWithNextTier() {
        // Pet owner 100 has 50 points (Bronze tier, requires 0)
        // Next tier is Silver (requires 100)
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(100L);

        assertNotNull(balance);
        assertEquals("Bronze", balance.currentTier());
        assertEquals("Silver", balance.nextTier());
        assertNotNull(balance.pointsToNextTier());
        assertEquals(50, balance.pointsToNextTier()); // 100 - 50 = 50
    }

    @Test
    public void testFidelityPointsBalanceHighTier() {
        // Pet owner 101 has 150 points (Silver tier)
        FidelityPointsBalanceDTO balance =
                petOwnerService.getFidelityPointsBalance(101L);

        assertNotNull(balance);
        assertEquals(150, balance.currentPoints());
        assertEquals("Silver", balance.currentTier());
        assertEquals("Gold", balance.nextTier());
        assertEquals(100, balance.pointsToNextTier()); // 250 - 150 = 100
    }
}
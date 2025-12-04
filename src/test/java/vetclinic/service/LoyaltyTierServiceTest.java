package vetclinic.service;

import vetclinic.application.LoyaltyTierService;
import vetclinic.application.exceptions.LoyaltyTierNotFoundException;
import vetclinic.application.inputDTO.LoyaltyTierCommand;
import vetclinic.application.outputDTO.LoyaltyTierInformation;
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
public class LoyaltyTierServiceTest {

    @Autowired
    private LoyaltyTierService loyaltyTierService;

    @Test
    public void testGetAllLoyaltyTiers() {
        List<LoyaltyTierInformation> tiers = loyaltyTierService.getAllLoyaltyTiers();

        assertNotNull(tiers);
        assertEquals(3, tiers.size()); // Bronze, Silver, Gold from data-test.sql

        // Verify tier names exist
        assertTrue(tiers.stream().anyMatch(t -> t.tierName().equals("Bronze")));
        assertTrue(tiers.stream().anyMatch(t -> t.tierName().equals("Silver")));
        assertTrue(tiers.stream().anyMatch(t -> t.tierName().equals("Gold")));
    }

    @Test
    public void testCreateLoyaltyTier() {
        LoyaltyTierCommand command = new LoyaltyTierCommand(
                "Platinum",
                500,
                20.0,
                "Premium tier with 20% discount"
        );

        Long tierId = loyaltyTierService.createLoyaltyTier(command);

        assertNotNull(tierId);

        LoyaltyTierInformation tier = loyaltyTierService.getLoyaltyTier(tierId);
        assertNotNull(tier);
        assertEquals("Platinum", tier.tierName());
        assertEquals(500, tier.requiredPoints());
        assertEquals(20.0, tier.discountPercentage());
        assertEquals("Premium tier with 20% discount", tier.benefitsDescription());
    }

    @Test
    public void testGetLoyaltyTier() {
        List<LoyaltyTierInformation> tiers = loyaltyTierService.getAllLoyaltyTiers();
        assertFalse(tiers.isEmpty());

        Long tierId = tiers.get(0).tierId();

        LoyaltyTierInformation tier = loyaltyTierService.getLoyaltyTier(tierId);
        assertNotNull(tier);
        assertEquals(tierId, tier.tierId());
    }

    @Test
    public void testUpdateLoyaltyTier() {
        List<LoyaltyTierInformation> tiers = loyaltyTierService.getAllLoyaltyTiers();

        // Get Bronze tier
        LoyaltyTierInformation bronze = tiers.stream()
                .filter(t -> t.tierName().equals("Bronze"))
                .findFirst()
                .orElseThrow();

        LoyaltyTierCommand updateCommand = new LoyaltyTierCommand(
                "Bronze",
                0,
                7.0, // Changed from 5.0 to 7.0
                "Updated entry level - 7% discount on services"
        );

        loyaltyTierService.updateLoyaltyTier(bronze.tierId(), updateCommand);

        LoyaltyTierInformation updated = loyaltyTierService.getLoyaltyTier(bronze.tierId());
        assertEquals(7.0, updated.discountPercentage());
        assertTrue(updated.benefitsDescription().contains("7%"));
    }

    @Test
    public void testDeleteLoyaltyTier() {
        // Create a tier to delete
        LoyaltyTierCommand command = new LoyaltyTierCommand(
                "Temporary",
                1000,
                25.0,
                "Temporary tier"
        );

        Long tierId = loyaltyTierService.createLoyaltyTier(command);

        // Verify it exists
        assertNotNull(loyaltyTierService.getLoyaltyTier(tierId));

        // Delete it
        loyaltyTierService.deleteLoyaltyTier(tierId);

        // Verify it's gone
        assertThrows(LoyaltyTierNotFoundException.class, () ->
                loyaltyTierService.getLoyaltyTier(tierId)
        );
    }

    @Test
    public void testGetNonExistentLoyaltyTier() {
        assertThrows(LoyaltyTierNotFoundException.class, () ->
                loyaltyTierService.getLoyaltyTier(999L)
        );
    }

    @Test
    public void testUpdateNonExistentLoyaltyTier() {
        LoyaltyTierCommand command = new LoyaltyTierCommand(
                "NonExistent",
                0,
                5.0,
                "Test"
        );

        assertThrows(LoyaltyTierNotFoundException.class, () ->
                loyaltyTierService.updateLoyaltyTier(999L, command)
        );
    }

    @Test
    public void testDeleteNonExistentLoyaltyTier() {
        assertThrows(LoyaltyTierNotFoundException.class, () ->
                loyaltyTierService.deleteLoyaltyTier(999L)
        );
    }
}
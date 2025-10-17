package vetclinic.service;

import vetclinic.application.LoyaltyTierService;
import vetclinic.application.inputDTO.LoyaltyTierCommand;
import vetclinic.application.outputDTO.LoyaltyTierInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = {
        "DELETE FROM discount",
        "DELETE FROM loyalty_tier",
        "ALTER TABLE loyalty_tier ALTER COLUMN tier_id RESTART WITH 1"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LoyaltyTierServiceTest {

    @Autowired
    private LoyaltyTierService loyaltyTierService;

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
        assertEquals(20.0, tier.discountPercentage());
    }

    @Test
    public void testGetAllLoyaltyTiers() {
        List<LoyaltyTierInformation> tiers = loyaltyTierService.getAllLoyaltyTiers();

        assertNotNull(tiers);
        assertTrue(tiers.size() >= 3); // Bronze, Silver, Gold from data.sql
    }
}
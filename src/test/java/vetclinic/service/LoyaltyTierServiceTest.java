package vetclinic.service;

import vetclinic.application.LoyaltyTierService;
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
public class LoyaltyTierServiceTest {

    @Autowired
    private LoyaltyTierService loyaltyTierService;

    @Test
    @Sql(scripts = "classpath:data-test.sql")
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
    @Sql(scripts = "classpath:data-test.sql")
    public void testGetAllLoyaltyTiers() {
        List<LoyaltyTierInformation> tiers = loyaltyTierService.getAllLoyaltyTiers();

        assertNotNull(tiers);
        assertEquals(3, tiers.size()); // Bronze, Silver, Gold from data-test.sql
    }
}
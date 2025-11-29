package vetclinic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import vetclinic.application.DiscountService;
import vetclinic.application.LoyaltyTierService;
import vetclinic.application.MedicationService;
import vetclinic.application.VeterinarianService;
import vetclinic.persistence.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class VetClinicApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private LoyaltyTierService loyaltyTierService;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private MedicationBatchRepository medicationBatchRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private LoyaltyTierRepository loyaltyTierRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should load successfully");
    }

    @Test
    void testServicesAreLoaded() {
        assertNotNull(veterinarianService, "VeterinarianService should be loaded");
        assertNotNull(medicationService, "MedicationService should be loaded");
        assertNotNull(discountService, "DiscountService should be loaded");
        assertNotNull(loyaltyTierService, "LoyaltyTierService should be loaded");
    }

    @Test
    void testRepositoriesAreLoaded() {
        assertNotNull(veterinarianRepository, "VeterinarianRepository should be loaded");
        assertNotNull(medicationRepository, "MedicationRepository should be loaded");
        assertNotNull(medicationBatchRepository, "MedicationBatchRepository should be loaded");
        assertNotNull(promotionRepository, "PromotionRepository should be loaded");
        assertNotNull(discountRepository, "DiscountRepository should be loaded");
        assertNotNull(loyaltyTierRepository, "LoyaltyTierRepository should be loaded");
        assertNotNull(availabilityRepository, "AvailabilityRepository should be loaded");
        assertNotNull(specialityRepository, "SpecialityRepository should be loaded");
    }

    @Test
    void testDatabaseIsPopulated() {
        // Check veterinarians
        long vetCount = veterinarianRepository.count();
        assertTrue(vetCount >= 2, "Should have at least 2 veterinarians from data.sql");

        // Check medications
        long medCount = medicationRepository.count();
        assertTrue(medCount >= 4, "Should have at least 4 medications from data.sql");

        // Check medication batches
        long batchCount = medicationBatchRepository.count();
        assertTrue(batchCount >= 5, "Should have at least 5 medication batches from data.sql");

        // Check loyalty tiers
        long tierCount = loyaltyTierRepository.count();
        assertEquals(3, tierCount, "Should have exactly 3 loyalty tiers (Bronze, Silver, Gold)");

        // Check promotions
        long promoCount = promotionRepository.count();
        assertTrue(promoCount >= 2, "Should have at least 2 promotions from data.sql");

        // Check specialities
        long specialityCount = specialityRepository.count();
        assertTrue(specialityCount >= 6, "Should have at least 6 specialities from data.sql");

        // Check availabilities
        long availCount = availabilityRepository.count();
        assertTrue(availCount >= 10, "Should have at least 10 availabilities from data.sql");
    }

    @Test
    void testVeterinarianServiceIntegration() {
        var availabilities = veterinarianService.getVeterinarianAvailabilities(1L);
        assertNotNull(availabilities, "Should be able to retrieve veterinarian availabilities");
        assertFalse(availabilities.isEmpty(), "Veterinarian 1 should have availabilities");
    }

    @Test
    void testMedicationServiceIntegration() {
        var lowStockMeds = medicationService.getLowStockMedications();
        assertNotNull(lowStockMeds, "Should be able to retrieve low stock medications");
    }

    @Test
    void testLoyaltyTierServiceIntegration() {
        var tiers = loyaltyTierService.getAllLoyaltyTiers();
        assertNotNull(tiers, "Should be able to retrieve loyalty tiers");
        assertEquals(3, tiers.size(), "Should have 3 loyalty tiers from data.sql");
    }

    @Test
    void testDiscountServiceIntegration() {
        var promotions = discountService.getAllPromotions();
        assertNotNull(promotions, "Should be able to retrieve promotions");
        assertTrue(promotions.size() >= 2, "Should have at least 2 promotions from data.sql");
    }

    @Test
    void testApplicationMainClassExists() {
        assertDoesNotThrow(() -> {
            Class<?> mainClass = Class.forName("vetclinic.VetClinicApplication");
            assertNotNull(mainClass, "VetClinicApplication class should exist");
        }, "VetClinicApplication class should be loadable");
    }
}
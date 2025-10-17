package vetclinic.application;

import vetclinic.application.outputDTO.LowStockMedicationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UC 2.10: Receive Low Stock Alert
 *
 * This component automatically checks medication stock levels periodically
 * and generates alerts when stock falls below the reorder threshold.
 */
@Component
public class LowStockScheduler {

    private static final Logger logger = LoggerFactory.getLogger(LowStockScheduler.class);

    private final MedicationService medicationService;

    public LowStockScheduler(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    /**
     * Scheduled task that runs every day at 8:00 AM
     * Checks for low stock medications and logs alerts
     */
    @Scheduled(cron = "0 0 8 * * ?") // Every day at 8:00 AM
    public void checkLowStockMedications() {
        logger.info("Running scheduled low stock check...");

        List<LowStockMedicationDTO> lowStockMeds = medicationService.getLowStockMedications();

        if (lowStockMeds.isEmpty()) {
            logger.info("No low stock medications found.");
        } else {
            logger.warn("LOW STOCK ALERT: Found {} medications below reorder threshold",
                    lowStockMeds.size());

            for (LowStockMedicationDTO med : lowStockMeds) {
                logger.warn("LOW STOCK - Medication: {} (ID: {}), Current Stock: {}, Threshold: {}",
                        med.medicationName(),
                        med.medicationId(),
                        med.totalCurrentQuantity(),
                        med.reorderThreshold());

                // Here you could:
                // - Send email notifications
                // - Create system notifications
                // - Update a notification dashboard
                // - Trigger automatic reordering
            }
        }
    }

    /**
     * Manual trigger for testing purposes
     * Can be called from a REST endpoint if needed
     */
    public List<LowStockMedicationDTO> triggerManualCheck() {
        logger.info("Manual low stock check triggered");
        return medicationService.getLowStockMedications();
    }
}
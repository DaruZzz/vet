package application.outputDTO;

import java.time.LocalDate;

public record MedicationBatchInformation(
        Long batchId,
        String medicationName,
        String lotNumber,
        LocalDate receivedDate,
        LocalDate expiryDate,
        Integer initialQuantity,
        Integer currentQuantity,
        Double purchasePricePerUnit,
        String storageLocation,
        boolean expired,
        boolean depleted
) {}

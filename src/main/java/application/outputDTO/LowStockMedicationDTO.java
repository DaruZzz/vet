package application.outputDTO;

public record LowStockMedicationDTO(
        Long medicationId,
        String medicationName,
        Long totalCurrentQuantity,
        Integer reorderThreshold
) {}

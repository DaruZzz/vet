package application.inputDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record MedicationBatchCommand(
        @NotNull Long medicationId,
        @NotBlank String lotNumber,
        @NotNull LocalDate receivedDate,
        @NotNull LocalDate expiryDate,
        @NotNull @Positive Integer initialQuantity,
        @NotNull @Positive Double purchasePricePerUnit,
        String storageLocation
) {}

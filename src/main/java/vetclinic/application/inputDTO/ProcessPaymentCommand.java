package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ProcessPaymentCommand(
        @NotNull Long invoiceId,
        @NotNull @Positive Double amount,
        @NotNull String paymentMethod,
        String transactionRef
) {}
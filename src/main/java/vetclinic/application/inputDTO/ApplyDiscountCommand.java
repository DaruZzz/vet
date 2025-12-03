package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotNull;

public record ApplyDiscountCommand(
        @NotNull String discountCode
) {}
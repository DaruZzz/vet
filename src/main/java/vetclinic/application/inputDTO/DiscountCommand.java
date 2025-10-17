package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record DiscountCommand(
        @NotBlank String code,
        @NotBlank String type,
        @NotNull @Positive Double value,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        Integer maxUses
) {}

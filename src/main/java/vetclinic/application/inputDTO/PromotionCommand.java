package vetclinic.application.inputDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PromotionCommand(
        @NotBlank String name,
        String description,
        String discountCode,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}

package application.outputDTO;

import java.time.LocalDate;

public record DiscountInformation(
        Long discountId,
        String code,
        String type,
        Double value,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxUses,
        Integer usesCount,
        boolean valid
) {}

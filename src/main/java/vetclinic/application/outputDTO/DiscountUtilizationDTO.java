package vetclinic.application.outputDTO;

import java.time.LocalDate;

public record DiscountUtilizationDTO(
        Long discountId,
        String discountCode,
        String discountType,
        Integer totalUses,
        Integer maxUses,
        Double totalAmountDiscounted,
        LocalDate startDate,
        LocalDate endDate,
        boolean isActive
) {}
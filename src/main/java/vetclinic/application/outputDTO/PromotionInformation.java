package vetclinic.application.outputDTO;

import java.time.LocalDate;
import java.util.List;

public record PromotionInformation(
        Long promotionId,
        String name,
        String description,
        String discountCode,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        List<DiscountInformation> discounts
) {}

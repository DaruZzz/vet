package application.mappers;

import application.inputDTO.PromotionCommand;
import application.outputDTO.PromotionInformation;
import domain.Promotion;

import java.util.stream.Collectors;

public class PromotionMapper {

    public static Promotion commandToDomain(PromotionCommand command) {
        Promotion promotion = new Promotion();
        promotion.setName(command.name());
        promotion.setDescription(command.description());
        promotion.setDiscountCode(command.discountCode());
        promotion.setStartDate(command.startDate());
        promotion.setEndDate(command.endDate());
        return promotion;
    }

    public static PromotionInformation toInformation(Promotion promotion) {
        return new PromotionInformation(
                promotion.getPromotionId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getDiscountCode(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.isActive(),
                promotion.getDiscounts().stream()
                        .map(DiscountMapper::toInformation)
                        .collect(Collectors.toList())
        );
    }
}

package application.mappers;

import application.inputDTO.DiscountCommand;
import application.outputDTO.DiscountInformation;
import domain.Discount;
import domain.DiscountType;

public class DiscountMapper {

    public static Discount commandToDomain(DiscountCommand command) {
        Discount discount = new Discount();
        discount.setCode(command.code());
        discount.setType(DiscountType.valueOf(command.type()));
        discount.setValue(command.value());
        discount.setStartDate(command.startDate());
        discount.setEndDate(command.endDate());
        discount.setMaxUses(command.maxUses());
        return discount;
    }

    public static DiscountInformation toInformation(Discount discount) {
        return new DiscountInformation(
                discount.getDiscountId(),
                discount.getCode(),
                discount.getType().name(),
                discount.getValue(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getMaxUses(),
                discount.getUsesCount(),
                discount.isValid()
        );
    }
}

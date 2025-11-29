package vetclinic.application.mappers;

import vetclinic.application.outputDTO.InvoiceItemInformation;
import vetclinic.domain.InvoiceItem;

public class InvoiceItemMapper {

    public static InvoiceItemInformation toInformation(InvoiceItem item) {
        return new InvoiceItemInformation(
                item.getItemId(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getItemTotal(),
                item.getItemType().name()
        );
    }
}
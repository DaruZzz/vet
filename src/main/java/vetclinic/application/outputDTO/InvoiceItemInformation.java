package vetclinic.application.outputDTO;

public record InvoiceItemInformation(
        Long itemId,
        String description,
        Integer quantity,
        Double unitPrice,
        Double itemTotal,
        String itemType
) {}
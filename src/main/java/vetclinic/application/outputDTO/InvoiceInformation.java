package vetclinic.application.outputDTO;

import java.time.LocalDate;
import java.util.List;

public record InvoiceInformation(
        Long invoiceId,
        Long petOwnerId,
        String petOwnerName,
        Long visitId,
        LocalDate invoiceDate,
        Double totalAmount,
        Double discountAmount,
        Double finalAmount,
        String status,
        List<InvoiceItemInformation> items
) {}
package vetclinic.application.outputDTO;

import java.time.LocalDate;

public record InvoiceHistoryDTO(
        Long invoiceId,
        LocalDate invoiceDate,
        String petOwnerName,
        Double totalAmount,
        Double discountAmount,
        Double finalAmount,
        String status
) {}
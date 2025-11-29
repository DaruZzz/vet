package vetclinic.application.mappers;

import vetclinic.application.outputDTO.InvoiceInformation;
import vetclinic.domain.Invoice;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceInformation toInformation(Invoice invoice) {
        return new InvoiceInformation(
                invoice.getInvoiceId(),
                invoice.getPetOwner().getPersonId(),
                invoice.getPetOwner().getFirstName() + " " + invoice.getPetOwner().getLastName(),
                invoice.getVisit() != null ? invoice.getVisit().getVisitId() : null,
                invoice.getInvoiceDate(),
                invoice.getTotalAmount(),
                invoice.getDiscountAmount(),
                invoice.getFinalAmount(),
                invoice.getStatus().name(),
                invoice.getItems().stream()
                        .map(InvoiceItemMapper::toInformation)
                        .collect(Collectors.toList())
        );
    }
}
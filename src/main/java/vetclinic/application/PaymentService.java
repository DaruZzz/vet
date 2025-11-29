package vetclinic.application;

import vetclinic.application.inputDTO.ProcessPaymentCommand;
import vetclinic.domain.*;
import vetclinic.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository,
                          InvoiceRepository invoiceRepository,
                          InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public Long processPayment(ProcessPaymentCommand command) {
        Invoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new RuntimeException(
                        "Invoice with id " + command.invoiceId() + " not found"
                ));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid");
        }

        Payment payment = new Payment();
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(command.amount());
        payment.setPaymentMethod(command.paymentMethod());
        payment.setTransactionRef(command.transactionRef());

        invoice.addPayment(payment);

        Payment saved = paymentRepository.save(payment);
        invoiceRepository.save(invoice);

        // If invoice is now paid, earn fidelity points
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            invoiceService.earnFidelityPoints(invoice.getInvoiceId());
        }

        return saved.getPaymentId();
    }
}
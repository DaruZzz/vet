package vetclinic.api;

import vetclinic.application.PaymentService;
import vetclinic.application.inputDTO.ProcessPaymentCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

    private final PaymentService paymentService;

    public PaymentRestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> processPayment(
            @RequestBody @Valid ProcessPaymentCommand command,
            UriComponentsBuilder uriBuilder) {

        Long paymentId = paymentService.processPayment(command);
        var location = uriBuilder
                .path("/api/payments/{id}")
                .buildAndExpand(paymentId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
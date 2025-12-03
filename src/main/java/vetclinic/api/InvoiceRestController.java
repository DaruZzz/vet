package vetclinic.api;

import org.springframework.format.annotation.DateTimeFormat;
import vetclinic.application.InvoiceService;
import vetclinic.application.inputDTO.SellMedicationCommand;
import vetclinic.application.outputDTO.DiscountUtilizationDTO;
import vetclinic.application.outputDTO.InvoiceHistoryDTO;
import vetclinic.application.outputDTO.InvoiceInformation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceRestController {

    private final InvoiceService invoiceService;

    public InvoiceRestController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    // UC 3.1: Generate Invoice from Visit
    @PostMapping("/from-visit/{visitId}")
    public ResponseEntity<Void> generateInvoiceFromVisit(
            @PathVariable Long visitId,
            UriComponentsBuilder uriBuilder) {

        Long invoiceId = invoiceService.generateInvoiceFromVisit(visitId);
        var location = uriBuilder
                .path("/api/invoices/{id}")
                .buildAndExpand(invoiceId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
    // UC 3.2: Sell Medication (Non-Visit Sale)
    @PostMapping("/sell-medication")
    public ResponseEntity<Void> sellMedication(
            @RequestBody @Valid SellMedicationCommand command,
            UriComponentsBuilder uriBuilder) {

        Long invoiceId = invoiceService.sellMedication(command);
        var location = uriBuilder
                .path("/api/invoices/{id}")
                .buildAndExpand(invoiceId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Get invoice details
    @GetMapping("/{invoiceId}")
    public InvoiceInformation getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    // UC 3.5: Trigger earning fidelity points after payment
    @PostMapping("/{invoiceId}/earn-points")
    public ResponseEntity<Void> earnFidelityPoints(@PathVariable Long invoiceId) {
        invoiceService.earnFidelityPoints(invoiceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public List<InvoiceHistoryDTO> getInvoiceHistory(
            @RequestParam(required = false) Long petOwnerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return invoiceService.getInvoiceHistory(petOwnerId, startDate, endDate);
    }

    @GetMapping("/discount-utilization")
    public List<DiscountUtilizationDTO> analyzeDiscountUtilization(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return invoiceService.analyzeDiscountUtilization(startDate, endDate);
    }
}
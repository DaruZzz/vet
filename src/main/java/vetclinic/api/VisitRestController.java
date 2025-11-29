package vetclinic.api;

import vetclinic.application.VisitService;
import vetclinic.application.inputDTO.*;
import vetclinic.application.outputDTO.ScheduleInformation;
import vetclinic.application.outputDTO.VisitInformation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/visits")
public class VisitRestController {

    private final VisitService visitService;

    public VisitRestController(VisitService visitService) {
        this.visitService = visitService;
    }

    // UC 1.1: Schedule New Visit
    @PostMapping
    public ResponseEntity<Void> scheduleVisit(
            @RequestBody @Valid ScheduleVisitCommand command,
            UriComponentsBuilder uriBuilder) {

        Long visitId = visitService.scheduleVisit(command);
        var location = uriBuilder
                .path("/api/visits/{id}")
                .buildAndExpand(visitId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Get visit details
    @GetMapping("/{visitId}")
    public VisitInformation getVisit(@PathVariable Long visitId) {
        return visitService.getVisit(visitId);
    }

    // UC 2.1: Start Consultation
    @PostMapping("/{visitId}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startConsultation(@PathVariable Long visitId) {
        visitService.startConsultation(visitId);
    }

    // UC 2.1: Complete Consultation
    @PostMapping("/{visitId}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeConsultation(@PathVariable Long visitId) {
        visitService.completeConsultation(visitId);
    }

    // UC 2.2: Record Diagnosis
    @PostMapping("/{visitId}/diagnosis")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recordDiagnosis(
            @PathVariable Long visitId,
            @RequestBody @Valid RecordDiagnosisCommand command) {

        visitService.recordDiagnosis(visitId, command);
    }

    // UC 2.3: Prescribe Medication
    @PostMapping("/{visitId}/prescriptions")
    public ResponseEntity<Void> prescribeMedication(
            @PathVariable Long visitId,
            @RequestBody @Valid PrescribeMedicationCommand command,
            UriComponentsBuilder uriBuilder) {

        Long prescriptionId = visitService.prescribeMedication(visitId, command);
        var location = uriBuilder
                .path("/api/prescriptions/{id}")
                .buildAndExpand(prescriptionId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // UC 2.4: Prescribe Treatment
    @PostMapping("/{visitId}/treatments")
    public ResponseEntity<Void> prescribeTreatment(
            @PathVariable Long visitId,
            @RequestBody @Valid PrescribeTreatmentCommand command,
            UriComponentsBuilder uriBuilder) {

        Long treatmentId = visitService.prescribeTreatment(visitId, command);
        var location = uriBuilder
                .path("/api/treatments/{id}")
                .buildAndExpand(treatmentId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}


package vetclinic.api;

import vetclinic.application.MedicationIncompatibilityService;
import vetclinic.application.inputDTO.CheckIncompatibilityCommand;
import vetclinic.application.inputDTO.CreateIncompatibilityCommand;
import vetclinic.application.inputDTO.UpdateIncompatibilityCommand;
import vetclinic.application.outputDTO.IncompatibilityCheckResponse;
import vetclinic.application.outputDTO.MedicationIncompatibilityDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/medication-incompatibilities")
public class MedicationIncompatibilityRestController {

    private final MedicationIncompatibilityService incompatibilityService;

    public MedicationIncompatibilityRestController(MedicationIncompatibilityService incompatibilityService) {
        this.incompatibilityService = incompatibilityService;
    }

    // UC 2.11: Create Incompatibility
    @PostMapping
    public ResponseEntity<Void> createIncompatibility(
            @RequestBody @Valid CreateIncompatibilityCommand command,
            UriComponentsBuilder uriBuilder) {

        Long incompatibilityId = incompatibilityService.createIncompatibility(command);

        var location = uriBuilder
                .path("/api/medication-incompatibilities/{id}")
                .buildAndExpand(incompatibilityId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // UC 2.11: Get incompatibilities for a specific medication
    @GetMapping("/medication/{medicationId}")
    public List<MedicationIncompatibilityDTO> getIncompatibilitiesForMedication(
            @PathVariable Long medicationId) {

        return incompatibilityService.getIncompatibilitiesForMedication(medicationId);
    }

    // UC 2.11: Get all incompatibilities
    @GetMapping
    public List<MedicationIncompatibilityDTO> getAllIncompatibilities() {
        return incompatibilityService.getAllIncompatibilities();
    }

    // UC 2.11: Update Incompatibility
    @PutMapping("/{incompatibilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIncompatibility(
            @PathVariable Long incompatibilityId,
            @RequestBody @Valid UpdateIncompatibilityCommand command) {

        incompatibilityService.updateIncompatibility(incompatibilityId, command);
    }

    // UC 2.11: Delete Incompatibility
    @DeleteMapping("/{incompatibilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncompatibility(@PathVariable Long incompatibilityId) {
        incompatibilityService.deleteIncompatibility(incompatibilityId);
    }

    // UC 2.12: Check incompatibility for pet and medication
    @PostMapping("/check")
    public IncompatibilityCheckResponse checkIncompatibility(
            @RequestBody @Valid CheckIncompatibilityCommand command) {

        boolean hasIncompatibility = incompatibilityService.checkIncompatibility(
                command.petId(),
                command.medicationId()
        );

        var alerts = incompatibilityService.getIncompatibilityAlerts(
                command.petId(),
                command.medicationId()
        );

        return new IncompatibilityCheckResponse(hasIncompatibility, alerts);
    }

    // UC 2.12: Alternative endpoint using query params
    @GetMapping("/check")
    public IncompatibilityCheckResponse checkIncompatibilityGet(
            @RequestParam Long petId,
            @RequestParam Long medicationId) {

        boolean hasIncompatibility = incompatibilityService.checkIncompatibility(
                petId,
                medicationId
        );

        var alerts = incompatibilityService.getIncompatibilityAlerts(
                petId,
                medicationId
        );

        return new IncompatibilityCheckResponse(hasIncompatibility, alerts);
    }
}
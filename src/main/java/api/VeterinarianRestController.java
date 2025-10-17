package api;

import application.VeterinarianService;
import application.inputDTO.AvailabilityCommand;
import application.inputDTO.AvailabilityExceptionCommand;
import application.outputDTO.AvailabilityInformation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarians")
public class VeterinarianRestController {

    private final VeterinarianService veterinarianService;

    public VeterinarianRestController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    // UC 1.7: Manage Veterinarian Availability

    @PostMapping("/{veterinarianId}/availabilities")
    public ResponseEntity<Void> addAvailability(
            @PathVariable Long veterinarianId,
            @RequestBody @Valid AvailabilityCommand command,
            UriComponentsBuilder uriBuilder) {

        Long availabilityId = veterinarianService.addAvailability(veterinarianId, command);
        var location = uriBuilder
                .path("/api/veterinarians/{vetId}/availabilities/{availId}")
                .buildAndExpand(veterinarianId, availabilityId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{veterinarianId}/availabilities")
    public List<AvailabilityInformation> getAvailabilities(@PathVariable Long veterinarianId) {
        return veterinarianService.getVeterinarianAvailabilities(veterinarianId);
    }

    @PutMapping("/{veterinarianId}/availabilities/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAvailability(
            @PathVariable Long veterinarianId,
            @PathVariable Long availabilityId,
            @RequestBody @Valid AvailabilityCommand command) {

        veterinarianService.updateAvailability(availabilityId, command);
    }

    @DeleteMapping("/{veterinarianId}/availabilities/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailability(
            @PathVariable Long veterinarianId,
            @PathVariable Long availabilityId) {

        veterinarianService.deleteAvailability(veterinarianId, availabilityId);
    }

    @PostMapping("/{veterinarianId}/availabilities/{availabilityId}/exceptions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAvailabilityException(
            @PathVariable Long veterinarianId,
            @PathVariable Long availabilityId,
            @RequestBody @Valid AvailabilityExceptionCommand command) {

        veterinarianService.addAvailabilityException(availabilityId, command);
    }
}
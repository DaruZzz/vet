package vetclinic.api;

import vetclinic.application.LoyaltyTierService;
import vetclinic.application.inputDTO.LoyaltyTierCommand;
import vetclinic.application.outputDTO.LoyaltyTierInformation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty-tiers")
public class LoyaltyTierRestController {

    private final LoyaltyTierService loyaltyTierService;

    public LoyaltyTierRestController(LoyaltyTierService loyaltyTierService) {
        this.loyaltyTierService = loyaltyTierService;
    }

    // UC 3.9: Define Loyalty Tiers

    @PostMapping
    public ResponseEntity<Void> createLoyaltyTier(
            @RequestBody @Valid LoyaltyTierCommand command,
            UriComponentsBuilder uriBuilder) {

        Long tierId = loyaltyTierService.createLoyaltyTier(command);
        var location = uriBuilder
                .path("/vetclinic/api/loyalty-tiers/{id}")
                .buildAndExpand(tierId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<LoyaltyTierInformation> getAllLoyaltyTiers() {
        return loyaltyTierService.getAllLoyaltyTiers();
    }

    @GetMapping("/{tierId}")
    public LoyaltyTierInformation getLoyaltyTier(@PathVariable Long tierId) {
        return loyaltyTierService.getLoyaltyTier(tierId);
    }

    @PutMapping("/{tierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLoyaltyTier(
            @PathVariable Long tierId,
            @RequestBody @Valid LoyaltyTierCommand command) {

        loyaltyTierService.updateLoyaltyTier(tierId, command);
    }

    @DeleteMapping("/{tierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoyaltyTier(@PathVariable Long tierId) {
        loyaltyTierService.deleteLoyaltyTier(tierId);
    }
}
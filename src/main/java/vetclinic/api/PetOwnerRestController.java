package vetclinic.api;

import vetclinic.application.PetOwnerService;
import vetclinic.application.outputDTO.FidelityPointsBalanceDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet-owners")
public class PetOwnerRestController {

    private final PetOwnerService petOwnerService;

    public PetOwnerRestController(PetOwnerService petOwnerService) {
        this.petOwnerService = petOwnerService;
    }

    // UC 3.11: View Fidelity Point Balance
    @GetMapping("/{petOwnerId}/fidelity-points")
    public FidelityPointsBalanceDTO getFidelityPointsBalance(@PathVariable Long petOwnerId) {
        return petOwnerService.getFidelityPointsBalance(petOwnerId);
    }
}
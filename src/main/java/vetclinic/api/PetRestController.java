package vetclinic.api;

import vetclinic.application.PetService;
import vetclinic.application.outputDTO.PetMedicalHistoryDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    private final PetService petService;

    public PetRestController(PetService petService) {
        this.petService = petService;
    }

    // UC 2.5: View Pet's Medical History
    @GetMapping("/{petId}/medical-history")
    public PetMedicalHistoryDTO getPetMedicalHistory(@PathVariable Long petId) {
        return petService.getPetMedicalHistory(petId);
    }
}

package vetclinic.api;

import vetclinic.application.DiscountService;
import vetclinic.application.inputDTO.DiscountCommand;
import vetclinic.application.inputDTO.PromotionCommand;
import vetclinic.application.outputDTO.DiscountInformation;
import vetclinic.application.outputDTO.PromotionInformation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DiscountRestController {

    private final DiscountService discountService;

    public DiscountRestController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // UC 3.8: Configure Promotions & Discounts

    @PostMapping("/promotions")
    public ResponseEntity<Void> createPromotion(
            @RequestBody @Valid PromotionCommand command,
            UriComponentsBuilder uriBuilder) {

        Long promotionId = discountService.createPromotion(command);
        var location = uriBuilder
                .path("/vetclinic/api/promotions/{id}")
                .buildAndExpand(promotionId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/promotions")
    public List<PromotionInformation> getAllPromotions() {
        return discountService.getAllPromotions();
    }

    @GetMapping("/promotions/{promotionId}")
    public PromotionInformation getPromotion(@PathVariable Long promotionId) {
        return discountService.getPromotion(promotionId);
    }

    @PutMapping("/promotions/{promotionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePromotion(
            @PathVariable Long promotionId,
            @RequestBody @Valid PromotionCommand command) {

        discountService.updatePromotion(promotionId, command);
    }

    @DeleteMapping("/promotions/{promotionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromotion(@PathVariable Long promotionId) {
        discountService.deletePromotion(promotionId);
    }

    @PostMapping("/promotions/{promotionId}/discounts")
    public ResponseEntity<Void> addDiscountToPromotion(
            @PathVariable Long promotionId,
            @RequestBody @Valid DiscountCommand command,
            UriComponentsBuilder uriBuilder) {

        Long discountId = discountService.addDiscountToPromotion(promotionId, command);
        var location = uriBuilder
                .path("/vetclinic/api/discounts/{id}")
                .buildAndExpand(discountId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/discounts/{discountId}")
    public DiscountInformation getDiscount(@PathVariable Long discountId) {
        return discountService.getDiscount(discountId);
    }

    @PutMapping("/discounts/{discountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDiscount(
            @PathVariable Long discountId,
            @RequestBody @Valid DiscountCommand command) {

        discountService.updateDiscount(discountId, command);
    }

    @DeleteMapping("/discounts/{discountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscount(@PathVariable Long discountId) {
        discountService.deleteDiscount(discountId);
    }
}
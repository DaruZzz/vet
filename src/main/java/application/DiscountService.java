package application;

import application.exceptions.DiscountNotFoundException;
import application.exceptions.PromotionNotFoundException;
import application.inputDTO.DiscountCommand;
import application.inputDTO.PromotionCommand;
import application.mappers.DiscountMapper;
import application.mappers.PromotionMapper;
import application.outputDTO.DiscountInformation;
import application.outputDTO.PromotionInformation;
import domain.Discount;
import domain.Promotion;
import persistence.DiscountRepository;
import persistence.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final PromotionRepository promotionRepository;

    public DiscountService(DiscountRepository discountRepository,
                           PromotionRepository promotionRepository) {
        this.discountRepository = discountRepository;
        this.promotionRepository = promotionRepository;
    }

    // UC 3.8: Configure Promotions & Discounts
    @Transactional
    public Long createPromotion(PromotionCommand command) {
        Promotion promotion = PromotionMapper.commandToDomain(command);
        Promotion saved = promotionRepository.save(promotion);
        return saved.getPromotionId();
    }

    @Transactional
    public void updatePromotion(Long promotionId, PromotionCommand command) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(
                        "Promotion with id " + promotionId + " not found"
                ));

        promotion.setName(command.name());
        promotion.setDescription(command.description());
        promotion.setDiscountCode(command.discountCode());
        promotion.setStartDate(command.startDate());
        promotion.setEndDate(command.endDate());

        promotionRepository.save(promotion);
    }

    @Transactional
    public void deletePromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(
                        "Promotion with id " + promotionId + " not found"
                ));

        promotionRepository.delete(promotion);
    }

    public PromotionInformation getPromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findByIdWithDiscounts(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(
                        "Promotion with id " + promotionId + " not found"
                ));

        return PromotionMapper.toInformation(promotion);
    }

    public List<PromotionInformation> getAllPromotions() {
        return StreamSupport.stream(promotionRepository.findAll().spliterator(), false)
                .map(PromotionMapper::toInformation)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long addDiscountToPromotion(Long promotionId, DiscountCommand command) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(
                        "Promotion with id " + promotionId + " not found"
                ));

        Discount discount = DiscountMapper.commandToDomain(command);
        promotion.addDiscount(discount);

        Discount saved = discountRepository.save(discount);
        return saved.getDiscountId();
    }

    @Transactional
    public void updateDiscount(Long discountId, DiscountCommand command) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new DiscountNotFoundException(
                        "Discount with id " + discountId + " not found"
                ));

        discount.setCode(command.code());
        discount.setValue(command.value());
        discount.setStartDate(command.startDate());
        discount.setEndDate(command.endDate());
        discount.setMaxUses(command.maxUses());

        discountRepository.save(discount);
    }

    @Transactional
    public void deleteDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new DiscountNotFoundException(
                        "Discount with id " + discountId + " not found"
                ));

        discountRepository.delete(discount);
    }

    public DiscountInformation getDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new DiscountNotFoundException(
                        "Discount with id " + discountId + " not found"
                ));

        return DiscountMapper.toInformation(discount);
    }
}
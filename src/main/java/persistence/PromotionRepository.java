package persistence;

import domain.Promotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PromotionRepository extends CrudRepository<Promotion, Long> {

    @Query("""
        SELECT p
        FROM Promotion p
        LEFT JOIN FETCH p.discounts
        WHERE p.promotionId = :id
        """)
    Optional<Promotion> findByIdWithDiscounts(Long id);
}

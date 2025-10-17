package vetclinic.persistence;

import vetclinic.domain.LoyaltyTier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoyaltyTierRepository extends CrudRepository<LoyaltyTier, Long> {

    Optional<LoyaltyTier> findByTierName(String tierName);

    @Query("""
        SELECT l
        FROM LoyaltyTier l
        LEFT JOIN FETCH l.discounts
        WHERE l.tierId = :id
        """)
    Optional<LoyaltyTier> findByIdWithDiscounts(Long id);
}

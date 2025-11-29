package vetclinic.persistence;

import vetclinic.domain.PetOwner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface PetOwnerRepository extends CrudRepository<PetOwner, Long> {

    @Query("""
        SELECT po
        FROM PetOwner po
        LEFT JOIN FETCH po.pets
        WHERE po.personId = :id
        """)
    Optional<PetOwner> findByIdWithPets(Long id);

    @Query("""
        SELECT po
        FROM PetOwner po
        LEFT JOIN FETCH po.loyaltyTier
        WHERE po.personId = :id
        """)
    Optional<PetOwner> findByIdWithLoyaltyTier(Long id);
}
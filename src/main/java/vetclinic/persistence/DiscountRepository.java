package vetclinic.persistence;

import vetclinic.domain.Discount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);
}

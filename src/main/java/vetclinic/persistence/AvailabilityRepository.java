package vetclinic.persistence;

import vetclinic.domain.Availability;
import org.springframework.data.repository.CrudRepository;

public interface AvailabilityRepository extends CrudRepository<Availability, Long> {
}

package vetclinic.persistence;

import vetclinic.domain.Treatment;
import org.springframework.data.repository.CrudRepository;

public interface TreatmentRepository extends CrudRepository<Treatment, Long> {
}
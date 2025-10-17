package vetclinic.persistence;

import vetclinic.domain.Speciality;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
    Optional<Speciality> findByName(String name);
}

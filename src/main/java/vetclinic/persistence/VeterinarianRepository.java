package vetclinic.persistence;

import vetclinic.domain.Veterinarian;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VeterinarianRepository extends CrudRepository<Veterinarian, Long> {

    @EntityGraph(attributePaths = {"availabilities"})
    @Query("SELECT DISTINCT v FROM Veterinarian v WHERE v.personId = :id")
    Optional<Veterinarian> findByIdWithAvailabilities(Long id);

    @EntityGraph(attributePaths = {"specialities"})
    @Query("SELECT DISTINCT v FROM Veterinarian v WHERE v.personId = :id")
    Optional<Veterinarian> findByIdWithSpecialities(Long id);
}
package persistence;

import domain.Veterinarian;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VeterinarianRepository extends CrudRepository<Veterinarian, Long> {

    @Query("""
        SELECT v
        FROM Veterinarian v
        LEFT JOIN FETCH v.availabilities a
        LEFT JOIN FETCH a.exceptions
        WHERE v.personId = :id
        """)
    Optional<Veterinarian> findByIdWithAvailabilities(Long id);

    @Query("""
        SELECT v
        FROM Veterinarian v
        LEFT JOIN FETCH v.specialities
        WHERE v.personId = :id
        """)
    Optional<Veterinarian> findByIdWithSpecialities(Long id);
}

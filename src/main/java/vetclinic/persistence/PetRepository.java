package vetclinic.persistence;

import vetclinic.domain.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PetRepository extends CrudRepository<Pet, Long> {

    // UC 2.5: Get pet with complete medical history
    @Query("""
        SELECT DISTINCT p
        FROM Pet p
        LEFT JOIN FETCH p.visits v
        LEFT JOIN FETCH v.treatments
        LEFT JOIN FETCH v.medicationPrescriptions mp
        LEFT JOIN FETCH mp.medication
        LEFT JOIN FETCH v.veterinarian
        WHERE p.petId = :id
        """)
    Optional<Pet> findByIdWithMedicalHistory(@Param("id") Long id);
}
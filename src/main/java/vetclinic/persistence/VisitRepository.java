package vetclinic.persistence;

import vetclinic.domain.Visit;
import vetclinic.domain.VisitStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends CrudRepository<Visit, Long> {

    @Query("""
        SELECT v
        FROM Visit v
        LEFT JOIN FETCH v.treatments
        LEFT JOIN FETCH v.medicationPrescriptions
        WHERE v.visitId = :id
        """)
    Optional<Visit> findByIdWithDetails(Long id);

    @Query("""
        SELECT v
        FROM Visit v
        WHERE v.veterinarian.personId = :veterinarianId
        AND v.dateTime BETWEEN :startDate AND :endDate
        ORDER BY v.dateTime
        """)
    List<Visit> findByVeterinarianAndDateRange(
            Long veterinarianId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Visit> findByStatus(VisitStatus status);
}
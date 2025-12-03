package vetclinic.persistence;

import org.springframework.data.repository.query.Param;
import vetclinic.application.outputDTO.SpecialityDemandDTO;
import vetclinic.application.outputDTO.VeterinarianDemandDTO;
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

    @Query("""
    SELECT new vetclinic.application.outputDTO.SpecialityDemandDTO(
        s.specialityId,
        s.name,
        COUNT(v.visitId)
    )
    FROM Visit v
    JOIN v.veterinarian vet
    JOIN vet.specialities s
    WHERE v.dateTime BETWEEN :startDate AND :endDate
    AND v.status = 'SCHEDULED'
    GROUP BY s.specialityId, s.name
    ORDER BY COUNT(v.visitId) DESC
    """)
    List<SpecialityDemandDTO> findSpecialitiesByDemand(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT new vetclinic.application.outputDTO.VeterinarianDemandDTO(
        vet.personId,
        CONCAT(vet.firstName, ' ', vet.lastName),
        COUNT(v.visitId)
    )
    FROM Visit v
    JOIN v.veterinarian vet
    WHERE v.dateTime BETWEEN :startDate AND :endDate
    AND v.status = 'SCHEDULED'
    GROUP BY vet.personId, vet.firstName, vet.lastName
    ORDER BY COUNT(v.visitId) DESC
    """)
    List<VeterinarianDemandDTO> findVeterinariansByDemand(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
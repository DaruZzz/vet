package vetclinic.persistence;

import vetclinic.domain.MedicationPrescription;
import org.springframework.data.repository.CrudRepository;

import vetclinic.application.outputDTO.MedicationPrescriptionStatsDTO;
import vetclinic.application.outputDTO.VeterinarianPrescriptionStatsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicationPrescriptionRepository extends CrudRepository<MedicationPrescription, Long> {

    // UC 2.6: List medications by prescription count
    @Query("""
        SELECT new vetclinic.application.outputDTO.MedicationPrescriptionStatsDTO(
            m.medicationId,
            m.name,
            COUNT(mp.prescriptionId)
        )
        FROM MedicationPrescription mp
        JOIN mp.medication m
        JOIN mp.visit v
        WHERE v.dateTime BETWEEN :startDate AND :endDate
        GROUP BY m.medicationId, m.name
        ORDER BY COUNT(mp.prescriptionId) DESC
        """)
    List<MedicationPrescriptionStatsDTO> findMedicationsByPrescriptionCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // UC 2.7: List veterinarians by medication prescription
    @Query("""
        SELECT new vetclinic.application.outputDTO.VeterinarianPrescriptionStatsDTO(
            vet.personId,
            CONCAT(vet.firstName, ' ', vet.lastName),
            COUNT(mp.prescriptionId)
        )
        FROM MedicationPrescription mp
        JOIN mp.visit v
        JOIN v.veterinarian vet
        WHERE mp.medication.medicationId = :medicationId
        AND v.dateTime BETWEEN :startDate AND :endDate
        GROUP BY vet.personId, vet.firstName, vet.lastName
        ORDER BY COUNT(mp.prescriptionId) DESC
        """)
    List<VeterinarianPrescriptionStatsDTO> findVeterinariansByMedicationPrescription(
            @Param("medicationId") Long medicationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
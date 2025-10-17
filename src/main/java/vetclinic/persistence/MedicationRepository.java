package vetclinic.persistence;

import vetclinic.application.outputDTO.LowStockMedicationDTO;
import vetclinic.domain.Medication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends CrudRepository<Medication, Long> {

    Optional<Medication> findByName(String name);

    @Query("""
        SELECT m
        FROM Medication m
        LEFT JOIN FETCH m.batches
        WHERE m.medicationId = :id
        """)
    Optional<Medication> findByIdWithBatches(Long id);

    @Query("""
        SELECT new application.outputDTO.LowStockMedicationDTO(
            m.medicationId, 
            m.name, 
            SUM(b.currentQuantity), 
            m.reorderThreshold
        )
        FROM Medication m
        LEFT JOIN m.batches b
        GROUP BY m.medicationId, m.name, m.reorderThreshold
        HAVING SUM(b.currentQuantity) < m.reorderThreshold
        """)
    List<LowStockMedicationDTO> findLowStockMedications();
}

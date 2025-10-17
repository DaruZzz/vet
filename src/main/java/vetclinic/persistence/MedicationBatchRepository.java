package vetclinic.persistence;

import vetclinic.domain.MedicationBatch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface MedicationBatchRepository extends CrudRepository<MedicationBatch, Long> {

    @Query("""
        SELECT b
        FROM MedicationBatch b
        WHERE b.medication.medicationId = :medicationId
        AND b.expiryDate > :date
        AND b.currentQuantity > 0
        ORDER BY b.expiryDate ASC
        """)
    List<MedicationBatch> findAvailableBatchesByMedicationId(Long medicationId, LocalDate date);
}

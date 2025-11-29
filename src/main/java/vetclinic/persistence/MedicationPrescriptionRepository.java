package vetclinic.persistence;

import vetclinic.domain.MedicationPrescription;
import org.springframework.data.repository.CrudRepository;

public interface MedicationPrescriptionRepository extends CrudRepository<MedicationPrescription, Long> {
}
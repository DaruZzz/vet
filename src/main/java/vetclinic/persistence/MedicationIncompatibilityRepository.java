package vetclinic.persistence;

import vetclinic.domain.MedicationIncompatibility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicationIncompatibilityRepository extends CrudRepository<MedicationIncompatibility, Long> {

    /**
     * Busca si existe una incompatibilidad entre dos medicaciones (en cualquier orden)
     */
    @Query("""
        SELECT mi FROM MedicationIncompatibility mi
        WHERE (mi.medication1.medicationId = :med1Id AND mi.medication2.medicationId = :med2Id)
           OR (mi.medication1.medicationId = :med2Id AND mi.medication2.medicationId = :med1Id)
        """)
    Optional<MedicationIncompatibility> findByMedicationPair(
            @Param("med1Id") Long med1Id,
            @Param("med2Id") Long med2Id
    );

    /**
     * Obtiene todas las incompatibilidades donde aparece una medicación específica
     */
    @Query("""
        SELECT mi FROM MedicationIncompatibility mi
        WHERE mi.medication1.medicationId = :medicationId
           OR mi.medication2.medicationId = :medicationId
        """)
    List<MedicationIncompatibility> findByMedicationId(@Param("medicationId") Long medicationId);
}
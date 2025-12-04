package vetclinic.domain;

import jakarta.persistence.*;

@Entity
public class MedicationIncompatibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incompatibilityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medication1_id", nullable = false)
    private Medication medication1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medication2_id", nullable = false)
    private Medication medication2;

    /**
     * Periodo de persistencia en días.
     * Si es null, las medicaciones son incompatibles siempre.
     * Si tiene valor, la incompatibilidad persiste ese número de días
     * después de la última toma de cualquiera de las dos medicaciones.
     */
    private Integer persistingPeriodDays;

    public MedicationIncompatibility() {
    }

    // Getters and Setters
    public Long getIncompatibilityId() {
        return incompatibilityId;
    }

    public void setIncompatibilityId(Long incompatibilityId) {
        this.incompatibilityId = incompatibilityId;
    }

    public Medication getMedication1() {
        return medication1;
    }

    public void setMedication1(Medication medication1) {
        this.medication1 = medication1;
    }

    public Medication getMedication2() {
        return medication2;
    }

    public void setMedication2(Medication medication2) {
        this.medication2 = medication2;
    }

    public Integer getPersistingPeriodDays() {
        return persistingPeriodDays;
    }

    public void setPersistingPeriodDays(Integer persistingPeriodDays) {
        this.persistingPeriodDays = persistingPeriodDays;
    }

    /**
     * Verifica si estas dos medicaciones son incompatibles entre sí
     */
    public boolean isIncompatibleWith(Long medicationId) {
        return medication1.getMedicationId().equals(medicationId) ||
                medication2.getMedicationId().equals(medicationId);
    }
}
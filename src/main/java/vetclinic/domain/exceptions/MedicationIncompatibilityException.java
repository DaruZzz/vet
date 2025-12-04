package vetclinic.domain.exceptions;

public class MedicationIncompatibilityException extends RuntimeException {
    public MedicationIncompatibilityException(String message) {
        super(message);
    }
}
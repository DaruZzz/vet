package vetclinic.application.exceptions;

public class MedicationIncompatibilityNotFoundException extends RuntimeException {
    public MedicationIncompatibilityNotFoundException(String message) {
        super(message);
    }
}

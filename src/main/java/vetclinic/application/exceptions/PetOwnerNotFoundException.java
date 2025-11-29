package vetclinic.application.exceptions;

public class PetOwnerNotFoundException extends RuntimeException {
    public PetOwnerNotFoundException(String message) {
        super(message);
    }
}
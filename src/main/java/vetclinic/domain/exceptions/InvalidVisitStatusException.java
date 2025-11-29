package vetclinic.domain.exceptions;

public class InvalidVisitStatusException extends RuntimeException {
    public InvalidVisitStatusException(String message) {
        super(message);
    }
}
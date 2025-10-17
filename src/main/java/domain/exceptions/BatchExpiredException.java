package domain.exceptions;

public class BatchExpiredException extends RuntimeException {
    public BatchExpiredException(String message) {
        super(message);
    }
}

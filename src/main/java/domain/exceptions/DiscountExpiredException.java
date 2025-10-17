package domain.exceptions;

public class DiscountExpiredException extends RuntimeException {
    public DiscountExpiredException(String message) {
        super(message);
    }
}

package domain.exceptions;

public class DiscountMaxUsesExceededException extends RuntimeException {
    public DiscountMaxUsesExceededException(String message) {
        super(message);
    }
}

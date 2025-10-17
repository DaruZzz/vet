package vetclinic.application.exceptions;

public class LoyaltyTierNotFoundException extends RuntimeException {
    public LoyaltyTierNotFoundException(String message) {
        super(message);
    }
}

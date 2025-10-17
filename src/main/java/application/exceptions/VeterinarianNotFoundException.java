package application.exceptions;

public class VeterinarianNotFoundException extends RuntimeException {
    public VeterinarianNotFoundException(String message) {
        super(message);
    }
}

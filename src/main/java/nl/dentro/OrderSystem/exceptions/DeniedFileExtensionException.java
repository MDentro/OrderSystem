package nl.dentro.OrderSystem.exceptions;

public class DeniedFileExtensionException extends RuntimeException {
    private static final long serialVersionUID = 5L;

    public DeniedFileExtensionException() {
        super();
    }

    public DeniedFileExtensionException(String message) {
        super(message);
    }
}

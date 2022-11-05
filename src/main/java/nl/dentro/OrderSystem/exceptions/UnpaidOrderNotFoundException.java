package nl.dentro.OrderSystem.exceptions;

public class UnpaidOrderNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UnpaidOrderNotFoundException() {
        super();
    }

    public UnpaidOrderNotFoundException(String message) {
        super(message);
    }

}

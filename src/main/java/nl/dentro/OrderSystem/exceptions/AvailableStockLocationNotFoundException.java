package nl.dentro.OrderSystem.exceptions;

public class AvailableStockLocationNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AvailableStockLocationNotFoundException() {
        super();
    }

    public AvailableStockLocationNotFoundException(String message) {
        super(message);
    }
}

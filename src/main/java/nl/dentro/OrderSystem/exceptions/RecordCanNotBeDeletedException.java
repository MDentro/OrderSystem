package nl.dentro.OrderSystem.exceptions;

public class RecordCanNotBeDeletedException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public RecordCanNotBeDeletedException() {
        super();
    }

    public RecordCanNotBeDeletedException(String message) {
        super(message);
    }
}

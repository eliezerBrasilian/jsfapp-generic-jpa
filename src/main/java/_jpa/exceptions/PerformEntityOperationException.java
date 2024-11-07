package _jpa.exceptions;

public class PerformEntityOperationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PerformEntityOperationException() {
        super("Was not possible to perform an operation on Entity at database");
    }

    public PerformEntityOperationException(String message) {
        super(message);
    }
}

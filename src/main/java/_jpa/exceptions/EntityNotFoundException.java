package _jpa.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException() {
        super("Entity was not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}

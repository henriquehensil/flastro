package ghostface.dev.exception;

public class NameAlreadyExists extends RuntimeException {
    public NameAlreadyExists(String message) {
        super(message);
    }
}

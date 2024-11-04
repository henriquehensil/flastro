package ghostface.dev.exception;

public class ColumnException extends Exception {
    public ColumnException(String message) {
        super(message);
    }

    public ColumnException(String message, Throwable cause) {
        super(message, cause);
    }
}

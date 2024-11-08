package ghostface.dev.exception;

public class DataTypeException extends Exception {
    public DataTypeException(String message) {
        super(message);
    }

    public DataTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

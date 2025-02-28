package codes.shawlas.data.exception;

public class IllegalMessageException extends IllegalArgumentException {
    public IllegalMessageException(String message) {
        super(message);
    }

    public IllegalMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}

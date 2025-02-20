package codes.shawlas.data.exception.message;

public class MessageStateException extends IllegalStateException {
    public MessageStateException(String message) {
        super(message);
    }

    public MessageStateException(String message, Throwable cause) {
        super(message, cause);
    }
}

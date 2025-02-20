package codes.shawlas.data.exception.message;

public class MessageReaderException extends RuntimeException {
    public MessageReaderException(String message) {
        super(message);
    }

    public MessageReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}

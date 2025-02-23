package codes.shawlas.data.exception.message;

public class MessageExecutorException extends RuntimeException {
    public MessageExecutorException(String message) {
        super(message);
    }

    public MessageExecutorException(String message, Throwable cause) {
        super(message, cause);
    }
}

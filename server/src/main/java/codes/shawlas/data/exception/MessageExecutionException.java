package codes.shawlas.data.exception;

public class MessageExecutionException extends RuntimeException {
    public MessageExecutionException(String message) {
        super(message);
    }

    public MessageExecutionException(String message, Throwable cause) {
      super(message, cause);
    }
}

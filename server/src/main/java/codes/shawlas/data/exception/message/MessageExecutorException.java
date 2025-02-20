package codes.shawlas.data.exception.message;

public class MessageExecutorException extends Exception {
    public MessageExecutorException(String message) {
        super(message);
    }

  public MessageExecutorException(String message, Throwable cause) {
    super(message, cause);
  }
}

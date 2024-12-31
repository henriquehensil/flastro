package codes.shawlas.data.exception;

public class MessageFinishException extends UnsupportedOperationException {
    public MessageFinishException() {
        super("This message has already been finalized");
    }

    public MessageFinishException(String message) {
        super(message);
    }
}

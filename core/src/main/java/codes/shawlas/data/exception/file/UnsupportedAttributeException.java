package codes.shawlas.data.exception.file;

public class UnsupportedAttributeException extends UnsupportedOperationException {
    public UnsupportedAttributeException(String message) {
        super(message);
    }

    public UnsupportedAttributeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedAttributeException(String message, Throwable cause) {
        super(message, cause);
    }
}

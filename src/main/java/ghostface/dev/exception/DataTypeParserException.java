package ghostface.dev.exception;

public class DataTypeParserException extends Exception {
    public DataTypeParserException(String message) {
        super(message);
    }

    public DataTypeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}

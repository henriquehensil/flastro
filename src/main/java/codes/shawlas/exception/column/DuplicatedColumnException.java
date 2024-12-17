package codes.shawlas.exception.column;

public class DuplicatedColumnException extends RuntimeException {
    public DuplicatedColumnException(String message) {
        super(message);
    }
}
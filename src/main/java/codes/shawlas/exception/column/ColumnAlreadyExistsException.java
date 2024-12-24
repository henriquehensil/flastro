package codes.shawlas.exception.column;

import codes.shawlas.exception.NameAlreadyExistsException;

public class ColumnAlreadyExistsException extends NameAlreadyExistsException {
    public ColumnAlreadyExistsException(String message) {
        super(message);
    }
}

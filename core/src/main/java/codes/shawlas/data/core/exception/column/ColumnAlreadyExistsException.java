package codes.shawlas.data.core.exception.column;

import codes.shawlas.data.core.exception.NameAlreadyExistsException;

public class ColumnAlreadyExistsException extends NameAlreadyExistsException {
    public ColumnAlreadyExistsException(String message) {
        super(message);
    }
}

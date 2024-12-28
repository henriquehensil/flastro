package codes.shawlas.data.core.exception.table;

import codes.shawlas.data.core.exception.NameAlreadyExistsException;

public class TableAlreadyExistsException extends NameAlreadyExistsException {
    public TableAlreadyExistsException(String message) {
        super(message);
    }
}

package codes.shawlas.exception.table;

import codes.shawlas.exception.NameAlreadyExistsException;

public class TableAlreadyExistsException extends NameAlreadyExistsException {
    public TableAlreadyExistsException(String message) {
        super(message);
    }
}

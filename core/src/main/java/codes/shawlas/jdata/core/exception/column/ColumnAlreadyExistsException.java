package codes.shawlas.jdata.core.exception.column;

import codes.shawlas.jdata.core.exception.NameAlreadyExistsException;

public class ColumnAlreadyExistsException extends NameAlreadyExistsException {
    public ColumnAlreadyExistsException(String message) {
        super(message);
    }
}

package codes.shawlas.jdata.core.exception.table;

import codes.shawlas.jdata.core.exception.NameAlreadyExistsException;

public class TableAlreadyExistsException extends NameAlreadyExistsException {
    public TableAlreadyExistsException(String message) {
        super(message);
    }
}

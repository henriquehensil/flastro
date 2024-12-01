package ghostface.dev.exception.table;

import ghostface.dev.exception.NameAlreadyExistsException;

public class TableAlreadyExistsException extends NameAlreadyExistsException {
    public TableAlreadyExistsException(String message) {
        super(message);
    }
}

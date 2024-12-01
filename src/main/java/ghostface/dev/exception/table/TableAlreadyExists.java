package ghostface.dev.exception.table;

import ghostface.dev.exception.NameAlreadyExistsException;

public class TableAlreadyExists extends NameAlreadyExistsException {
    public TableAlreadyExists(String message) {
        super(message);
    }
}

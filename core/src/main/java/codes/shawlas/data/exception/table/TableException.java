package codes.shawlas.data.exception.table;

import org.jetbrains.annotations.NotNull;

public class TableException extends Exception {
    public TableException(String message) {
        super(message);
    }

    public TableException(String message, Throwable cause) {
        super(message, cause);
    }
}

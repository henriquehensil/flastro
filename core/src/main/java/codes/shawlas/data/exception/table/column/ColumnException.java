package codes.shawlas.data.exception.table.column;

import org.jetbrains.annotations.NotNull;

public class ColumnException extends Exception {
    public ColumnException(@NotNull String message) {
        super(message);
    }
    public ColumnException(@NotNull String message, Throwable cause) {
        super(message, cause);
    }

}

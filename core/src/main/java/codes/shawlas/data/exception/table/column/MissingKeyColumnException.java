package codes.shawlas.data.exception.table.column;

import codes.shawlas.data.table.standard.Column;
import org.jetbrains.annotations.NotNull;

/**
 * Throw when a column key is missing on
 * */
public class MissingKeyColumnException extends ColumnException {
    public MissingKeyColumnException(@NotNull String message) {
        super(message);
    }

    public MissingKeyColumnException(@NotNull Column<?> column) {
        super("Column key is missing: " + column);
        if (!column.isKey()) {
            throw new IllegalArgumentException("This column must to be an Key");
        }
    }
}

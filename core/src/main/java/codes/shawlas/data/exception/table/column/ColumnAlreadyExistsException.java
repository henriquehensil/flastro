package codes.shawlas.data.exception.table.column;

import codes.shawlas.data.table.Column;
import org.jetbrains.annotations.NotNull;

/**
 * throws if column already exists
 * */
public class ColumnAlreadyExistsException extends ColumnException {
    public ColumnAlreadyExistsException(@NotNull Column<?> column) {
      super("Column already exists: " + column);
    }
}
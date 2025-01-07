package codes.shawlas.data.exception.column;

import codes.shawlas.data.table.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * indicates that column does not accept the informed value due to the attribute type
 * <P>If a column is a key, the value cannot be changed.
 * If is not nullable, a null value is not accept</P>
 * */
public class ColumnTypeException extends ColumnException {
    public ColumnTypeException(@NotNull Column<?> column, @Nullable Object value) {
        super("Cannot accept the value: '" + value + "'because the column " + (column.isKey() ? "is key" : "not accept null values"));
    }

    public ColumnTypeException(@NotNull String message) {
        super(message);
    }
}

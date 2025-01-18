package codes.shawlas.data.exception.table.column;

import codes.shawlas.data.table.Column;
import org.jetbrains.annotations.NotNull;

/**
 * Indicates that column doest not present in the table
 * */
public class InvalidColumnException extends ColumnException {
    public InvalidColumnException(@NotNull Column<?> column) {
        super("the column '" + column + "' is not present on this table");
    }
}

package codes.shawlas.data.exception.table.column;

import org.jetbrains.annotations.NotNull;

/**
 * indicates that column does not accept the informed value due to the attribute type
 * <P>If a column is a key, the value cannot be changed.
 * If is not nullable, a null value is not accept</P>
 * */
public class ColumnAttributeException extends ColumnException {
    public ColumnAttributeException(@NotNull String message) {
        super(message);
    }
}

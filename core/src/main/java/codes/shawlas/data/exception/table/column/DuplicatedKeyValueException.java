package codes.shawlas.data.exception.table.column;

import org.jetbrains.annotations.NotNull;

public class DuplicatedKeyValueException extends ColumnException {
    public DuplicatedKeyValueException(@NotNull String message) {
        super(message);
    }

    public DuplicatedKeyValueException(@NotNull Object value) {
        super("The key value is already in use: " + value);
    }
}

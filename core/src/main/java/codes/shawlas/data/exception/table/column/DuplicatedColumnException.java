package codes.shawlas.data.exception.table.column;

import org.jetbrains.annotations.NotNull;

public class DuplicatedColumnException extends ColumnException {
    public DuplicatedColumnException(@NotNull String message) {
        super(message);
    }
}
package codes.shawlas.data.exception.table;

import org.jetbrains.annotations.NotNull;

public class EmptyTableException extends TableException {
    public EmptyTableException(@NotNull String message) {
        super(message);
    }
}

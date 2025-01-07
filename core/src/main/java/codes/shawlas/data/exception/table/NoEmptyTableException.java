package codes.shawlas.data.exception.table;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates that the table cannot perform operations when
 * is in the state where there are elements
 * */
public class NoEmptyTableException extends TableException {
    public NoEmptyTableException(@NotNull String message) {
        super(message);
    }
}

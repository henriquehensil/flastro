package codes.shawlas.data.exception;

import org.jetbrains.annotations.NotNull;

public class TableException extends Exception {
    public TableException(String message) {
        super(message);
    }

    // Classes

    public static class EmptyTableException extends TableException {
        public EmptyTableException(@NotNull String message) {
            super(message);
        }
    }

    /**
     * Indicates that the table cannot perform operations when
     * is in the state where there are elements
     * */
    public static class NoEmptyTableException extends TableException {
        public NoEmptyTableException(@NotNull String message) {
            super(message);
        }
    }
}

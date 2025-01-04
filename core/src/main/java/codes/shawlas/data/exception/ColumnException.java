package codes.shawlas.data.exception;

import codes.shawlas.data.table.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ColumnException extends Exception {
    public ColumnException(@NotNull String message) {
        super(message);
    }
    public ColumnException(@NotNull String message, Throwable cause) {
        super(message, cause);
    }

    // Classes

    /**
     * indicates that column does not accept the informed value due to the attribute type
     * <P>If a column is a key, the value cannot be changed.
     * If is not nullable, a null value is not accept</P>
     * */
    public static class ColumnTypeException extends ColumnException {
        public ColumnTypeException(@NotNull Column<?> column, @Nullable Object value) {
            super("Cannot accept the value: '" + value + "'because the column " + (column.isKey() ? "is key" : "not accept null values"));
        }
    }

    /**
     * Indicates that column doest not present in the table
     * */
    public static class InvalidColumnException extends ColumnException {
        public InvalidColumnException(@NotNull Column<?> column) {
            super("the column '" + column +"' is not present on this table");
        }
    }

    /**
     * Throw when a column key is missing on
     * */
    public static class MissingKeyColumnException extends ColumnException {
        public MissingKeyColumnException(@NotNull Column<?> column) {
            super("Column key is missing: " + column);
            if (!column.isKey()) {
                throw new IllegalArgumentException("This column must to be an Key");
            }
        }
        public MissingKeyColumnException(@NotNull Column<?> @NotNull ... columns) {
            super("Column keys is missing: " + Arrays.toString(columns));
            if (Arrays.stream(columns).anyMatch(Column::isKey)) {
                throw new IllegalArgumentException("Some column must to be an Key");
            }
        }
    }

    /**
     * throws if column already exists
     * */
    public static class ColumnAlreadyExistsException extends ColumnException {
        public ColumnAlreadyExistsException(@NotNull Column<?> column) {
            super("Column already exists: " + column);
        }
    }
}

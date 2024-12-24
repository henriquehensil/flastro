package codes.shawlas.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public final class TableData<T> {

    private final @NotNull Column<T> column;
    private final @UnknownNullability T value;

    public TableData(@NotNull Column<T> column, @UnknownNullability T value) {
        if (column.isKey() && value == null) {
            throw new IllegalArgumentException("Key value cannot be null");
        } else if (!column.isNullable() && value == null) {
            throw new IllegalArgumentException("Column does not accept nullable value");
        } else {
            this.column = column;
            this.value = value;
        }
    }

    public @NotNull Column<T> getColumn() {
        return column;
    }

    public @UnknownNullability T getValue() {
        return value;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull TableData<?> tableData = (TableData<?>) o;
        return Objects.equals(column, tableData.column);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(column);
    }
}

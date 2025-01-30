package codes.shawlas.data.table.standard;

import codes.shawlas.data.exception.table.column.ColumnAttributeException;
import codes.shawlas.data.exception.table.column.ColumnException;
import codes.shawlas.data.exception.table.column.InvalidColumnException;
import codes.shawlas.data.table.EntryData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Objects;

public interface Element {

    @NotNull Table getTable();

    @NotNull String getId();

    @Range(from = 0, to = Long.MAX_VALUE) int getRow();

    /**
     * @throws InvalidColumnException if the column does not belong to this table
     * */
    <E> @Nullable E getValue(@NotNull Column<E> column) throws InvalidColumnException;

    /**
     * @throws InvalidColumnException if the column does not belong to this table
     * @throws ColumnAttributeException if the column attribute, like Key or Nullable, doest not matches with {@code value} parameter
     * */
    <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws ColumnException;

    default boolean containsValue(@Nullable Object value) {
        return getData().stream().anyMatch(e -> Objects.equals(e.getValue(), value));
    }

    @Unmodifiable @NotNull Collection<@NotNull EntryData<?>> getData();

}

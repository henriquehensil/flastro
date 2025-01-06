package codes.shawlas.data.table;

import codes.shawlas.data.exception.ColumnException;
import codes.shawlas.data.exception.ColumnException.InvalidColumnException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Objects;

public interface Element {

    @NotNull Table getTable();

    @Range(from = 0, to = Long.MAX_VALUE) int getIndex();

    /**
     * @throws InvalidColumnException if {@code column} does not exists
     * */
    <E> @Nullable E getValue(@NotNull Column<E> column) throws InvalidColumnException;

    /**
     * @throws ColumnException if any column errors occurs
     * */
    <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws ColumnException;

    default boolean containsValue(@Nullable Object value) {
        return getData().stream().anyMatch(data -> Objects.equals(data.getValue(), value));
    }

    @Unmodifiable @NotNull Collection<@NotNull EntryData<?>> getData();
}

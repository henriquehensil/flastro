package codes.shawlas.data.table;

import codes.shawlas.data.exception.column.ColumnException;
import codes.shawlas.data.exception.column.InvalidColumnException;
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

    boolean containsValue(@Nullable Object value);

    @Unmodifiable @NotNull Collection<@NotNull EntryData<?>> getData();
}

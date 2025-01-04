package codes.shawlas.data.table;

import codes.shawlas.data.content.EntryData;
import codes.shawlas.data.exception.ColumnException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Element {

    @NotNull Table getTable();

    @Range(from = 0, to = Long.MAX_VALUE) int getIndex();

    <E> @Nullable E getValue(@NotNull Column<E> column);

    /**
     * @throws ColumnException if any column errors occurs
     * */
    <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws ColumnException;

    @Unmodifiable @NotNull Collection<@NotNull EntryData<?>> getData();
}

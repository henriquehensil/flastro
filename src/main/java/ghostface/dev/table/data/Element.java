package ghostface.dev.table.data;

import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.exception.column.ColumnException;
import ghostface.dev.exception.column.InvalidColumnException;
import ghostface.dev.table.Data;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Element extends UnmodifiableContent<@NotNull Data<?>> {

    @NotNull String getId();

    @NotNull Table getTable();

    /**
     * @throws IllegalArgumentException if the column does not exist
     * */
    <E> @Nullable E get(@NotNull Column<E> column) throws IllegalArgumentException;

    /**
     * @throws InvalidColumnException if the {@code columnId} does not exist in Table#getColumns.
     * @throws ColumnException if the {@code value} is null and the column is either non-nullable or a key column.
     * */
    <E> void set(@NotNull String columnId, @Nullable E value) throws InvalidColumnException, ColumnException;

    @Override
    @Unmodifiable @NotNull Collection<@NotNull Data<?>> toCollection();
}
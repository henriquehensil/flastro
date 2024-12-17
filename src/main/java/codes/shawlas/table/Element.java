package codes.shawlas.table;

import codes.shawlas.content.UnmodifiableContent;
import codes.shawlas.exception.column.ColumnException;
import codes.shawlas.exception.column.InvalidColumnException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Element extends UnmodifiableContent<@NotNull TableData<?>> {

    @NotNull String getId();

    @NotNull Table getTable();

    /**
     * @throws InvalidColumnException if the {@code column id} does not present
     * */
    <E> @Nullable E get(@NotNull String columnId) throws InvalidColumnException;

    /**
     * @throws InvalidColumnException if the {@code columnId} does not exist in Table#getColumns.
     * @throws ColumnException if the {@code value} is null and the column is either non-nullable or a key column.
     * */
    <E> void set(@NotNull String columnId, @Nullable E value) throws InvalidColumnException, ColumnException;

    @Override
    @Unmodifiable @NotNull Collection<@NotNull TableData<?>> toCollection();
}
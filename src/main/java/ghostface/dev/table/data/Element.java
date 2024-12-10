package ghostface.dev.table.data;

import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.exception.column.ColumnException;
import ghostface.dev.exception.column.InvalidColumnException;
import ghostface.dev.table.TableData;
import ghostface.dev.table.Table;
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
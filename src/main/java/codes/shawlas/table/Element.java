package codes.shawlas.table;

import codes.shawlas.exception.column.ColumnException;
import codes.shawlas.exception.column.InvalidColumnException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Element {

    @NotNull String getId();

    @NotNull Table getTable();

    /**
     * @throws InvalidColumnException if the {@code column} does not present
     * */
    <E> @Nullable E get(@NotNull Column<E> column) throws InvalidColumnException;

    /**
     * @throws InvalidColumnException if the {@code column} does not exist in Table#getColumns.
     * @throws ColumnException if the {@code value} is null and the column is either non-nullable or a key column.
     * */
    <E> void set(@NotNull Column<E> column, @Nullable E value) throws InvalidColumnException, ColumnException;

    @Unmodifiable @NotNull Collection<@NotNull TableData<?>> getData();

}
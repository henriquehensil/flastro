package codes.shawlas.data.table;

import codes.shawlas.data.DataType;
import codes.shawlas.data.exception.table.NoEmptyTableException;
import codes.shawlas.data.exception.table.column.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Table extends Serializable {

    @NotNull String getName();

    @NotNull Elements getElements();

    @NotNull Columns getColumns();

    // Classes

    interface Elements extends Serializable {

        @NotNull Table getTable();

        /**
         * @throws NoColumnsException if not have any columns
         * @throws DuplicatedColumnException when {@code entryData} has duplicated columns
         * @throws MissingKeyColumnException if at least 1 key column is not specified
         * @throws InvalidColumnException when {@code entryData} has Columns that not present in this table
         * @throws DuplicatedKeyValueException when some key column value is already defined in another element
         * */
        @NotNull Element create(@NotNull EntryData<?> @NotNull ... entryData)
                throws NoColumnsException, DuplicatedColumnException, MissingKeyColumnException, InvalidColumnException, DuplicatedKeyValueException;

        @NotNull Optional<? extends @NotNull Element> get(int row);

        boolean delete(int row);

        @Unmodifiable @NotNull Collection<? extends @NotNull Element> getAll();
    }

    interface Columns extends Serializable {

        @NotNull Table getTable();

        /**
         * @return A key column created
         * @throws NoEmptyTableException if you have an element in the table, you can't create a column key
         * @throws ColumnAlreadyExistsException if column {@code name} already exists
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, NoEmptyTableException;

        /**
         * @return A standard column created
         * @throws ColumnException if any column errors occurs
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E defaultValue, boolean isNullable) throws ColumnException;

        @NotNull Optional<? extends @NotNull Column<?>> get(@NotNull String columnName);

        /**
         * @throws NoEmptyTableException when has
         * */
        boolean delete(@NotNull String columnName) throws NoEmptyTableException;

        @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getAll();

        default @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getKeys() {
            return Collections.unmodifiableCollection(getAll().stream().filter(Column::isKey).collect(Collectors.toList()));
        }
    }
}

package codes.shawlas.data.table.standard;

import codes.shawlas.data.io.DataType;
import codes.shawlas.data.exception.table.NoEmptyTableException;
import codes.shawlas.data.exception.table.TableException;
import codes.shawlas.data.exception.table.column.*;
import codes.shawlas.data.table.EntryData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Table {

    @NotNull String getId();

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
        @NotNull Element create(@NotNull EntryData<?> @NotNull ... entryData) throws ColumnException;

        @NotNull Optional<? extends @NotNull Element> get(int row);

        @NotNull Optional<? extends @NotNull Element> get(@NotNull String id);

        boolean delete(int row);

        boolean delete(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Element> getAll();

    }

    interface Columns extends Serializable {

        @NotNull Table getTable();

        /**
         * @return A key column created
         * @throws NoEmptyTableException if you have an element in the table, you can't create a column key
         * @throws ColumnAlreadyExistsException if column {@code name} already exists
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnException, TableException;

        /**
         * @return A standard column created
         *
         * @throws ColumnAlreadyExistsException if column {@code name} already exists
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E defaultValue, boolean isNullable) throws ColumnAlreadyExistsException;

        @NotNull Optional<? extends @NotNull Column<?>> get(@NotNull String column);

        boolean delete(@NotNull String column);

        @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getAll();

        default @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getKeys() {
            return getAll().stream().filter(Column::isKey).collect(Collectors.toList());
        }

        default @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getNullable() {
            return getAll().stream().filter(Column::isNullable).collect(Collectors.toList());
        }
    }
}

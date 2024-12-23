package codes.shawlas.table;

import codes.shawlas.DataType;
import codes.shawlas.exception.column.ColumnAlreadyExistsException;
import codes.shawlas.exception.key.DuplicatedKeyValueException;
import codes.shawlas.exception.key.MissingKeyException;
import codes.shawlas.exception.table.TableStateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Table {

    @NotNull String getId();

    @NotNull Elements getElements();

    @NotNull Columns getColumns();

    // Classes

    interface Elements {

        @NotNull Table getTable();

        /**
         * @throws MissingKeyException If the key is missing
         * @throws DuplicatedKeyValueException If the key value already exists
         * @throws TableStateException If table#getColumns is empty
         * */
        @NotNull Element create(@NotNull TableData<?> @NotNull ... data) throws MissingKeyException, DuplicatedKeyValueException, TableStateException;

        @NotNull Optional<? extends @NotNull Element> get(long index);

        @NotNull Optional<? extends @NotNull Element> getById(@NotNull String id);

        boolean remove(long index);

        boolean removeById(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Element> toCollection();
    }

    interface Columns {

        @NotNull Table getTable();

        /**
         * @throws ColumnAlreadyExistsException if name is already in use
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws ColumnAlreadyExistsException;

        /**
         * @throws ColumnAlreadyExistsException if the name is already in use
         * @throws TableStateException if the table elements is not empty
         * */
        <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, TableStateException;

        @NotNull Optional<? extends @NotNull Column<?>> get(@NotNull String name);

        @NotNull Optional<? extends @NotNull Column<?>> getById(@NotNull String id);

        boolean remove(@NotNull String name);

        boolean removeById(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> toCollection();
    }
}

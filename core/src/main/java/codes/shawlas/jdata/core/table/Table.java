package codes.shawlas.jdata.core.table;

import codes.shawlas.jdata.core.DataType;
import codes.shawlas.jdata.core.content.NamedContent;
import codes.shawlas.jdata.core.exception.InvalidNameException;
import codes.shawlas.jdata.core.exception.column.*;
import codes.shawlas.jdata.core.exception.table.TableStateException;
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
         * @throws InvalidColumnException if {@code data} has a Column which is not in the table
         * @throws DuplicatedColumnException if data has duplicated column
         * */
        @NotNull Element create(@NotNull TableData<?> @NotNull ... data) throws MissingKeyException, DuplicatedKeyValueException, TableStateException, InvalidColumnException, DuplicatedColumnException;

        @NotNull Optional<? extends @NotNull Element> get(long index);

        @NotNull Optional<? extends @NotNull Element> getById(@NotNull String id);

        boolean delete(long index);

        boolean deleteById(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Element> toCollection();
    }

    interface Columns extends NamedContent<@NotNull Column<?>> {

        @NotNull Table getTable();

        /**
         * @throws ColumnAlreadyExistsException if the {@code name} is already in use
         * @throws InvalidNameException if the {@code name} is an invalid syntax
         * */
        <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws ColumnAlreadyExistsException, InvalidNameException;

        /**
         * @throws ColumnAlreadyExistsException if the {@code name}is already in use
         * @throws TableStateException if the table elements is not empty
         * @throws InvalidNameException if the {@code name} is an invalid syntax
         * */
        <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, TableStateException, InvalidNameException;

        @NotNull Optional<? extends @NotNull Column<?>> get(@NotNull String name);

        @NotNull Optional<? extends @NotNull Column<?>> getById(@NotNull String id);

        boolean delete(@NotNull String name);

        boolean deleteById(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> toCollection();
    }
}

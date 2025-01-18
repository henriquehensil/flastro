package codes.shawlas.data.table;

import codes.shawlas.data.exception.table.column.ColumnTypeException;
import codes.shawlas.data.exception.table.column.InvalidColumnException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * A Table with support to a retrieval of data operations.
 * The indexed Table save the data into a B-tree structure
 * where the elements are filtered by values and increase the data retrieval
 * */
public interface IndexedTable extends Table {

    @Override
    @NotNull IndexedElements getElements();

    @Override
    @NotNull IndexedColumns getColumns();

    // Classes

    interface IndexedElements extends Elements {

        @NotNull IndexedTable getTable();

        /**
         * @throws InvalidColumnException if {@code data#getColumn} does not exists in this table
         * */
        @NotNull Collection<? extends @NotNull Element> getFiltered(@NotNull EntryData<?> value) throws InvalidColumnException;

        /**
         * @throws InvalidColumnException if {@code data#getColumn} does not exists in this table
         * @throws ColumnTypeException if {@code data#getColumn} is not a key
         * */
        @NotNull Optional<? extends @NotNull Element> getUnique(@NotNull EntryData<?> data) throws InvalidColumnException, ColumnTypeException;

        /**
         * @throws InvalidColumnException if {@code data#getColumn} does not exists in this table
         * */
        boolean deleteFiltered(@NotNull EntryData<?> data) throws InvalidColumnException;

        /**
         * @throws InvalidColumnException if {@code data#getColumn} does not exists in this table
         * @throws ColumnTypeException if {@code data#getColumn} is not a key
         * */
        boolean deleteUnique(@NotNull EntryData<?> data) throws ColumnTypeException, InvalidColumnException;

    }

    interface IndexedColumns extends Columns {

        @NotNull IndexedTable getTable();

        @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> getIndexed();

    }

    interface Indexes {

        @NotNull IndexedTable getTable();

        boolean add(@NotNull Column<?> column) throws InvalidColumnException ;

        boolean stop(@NotNull Column<?> column) throws InvalidColumnException;

        boolean delete(@NotNull Column<?> column) throws InvalidColumnException;

    }
}
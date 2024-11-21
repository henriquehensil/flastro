package ghostface.dev.table.column;

import ghostface.dev.DataType;
import ghostface.dev.table.Key;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import ghostface.dev.exception.NameAlreadyExists;
import ghostface.dev.exception.table.TableStateException;
import java.util.Collection;
import java.util.Optional;

public interface Columns {

    /**
     * @throws NameAlreadyExists if name is already in use
     * @throws IllegalArgumentException if the value is null and the isNullable parameter is false
     * */
    <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws NameAlreadyExists, IllegalArgumentException;

    /**
     * @throws NameAlreadyExists if the name is already in use
     * @throws TableStateException if the table elements is not empty
     * */
    <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws NameAlreadyExists, TableStateException;

    /**
     * @return the Key associated with the specified value
     * @throws IllegalArgumentException if the Key's column name does not exist in these columns
     * */
    <E> @NotNull Key<E> getKey(@NotNull String name, @NotNull E value) throws IllegalArgumentException;

    // Getters

    @NotNull Table getTable();

    @NotNull Optional<? extends Column<?>> get(@NotNull String name);

    @Unmodifiable @NotNull Collection<? extends Column<?>> toCollection();
}

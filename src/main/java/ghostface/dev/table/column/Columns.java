package ghostface.dev.table.column;

import ghostface.dev.DataType;
import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.exception.table.TableStateException;
import java.util.Collection;
import java.util.Optional;

public interface Columns extends UnmodifiableContent<@NotNull Column<?>> {

    /**
     * @throws NameAlreadyExistsException if name is already in use
     * */
    <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws NameAlreadyExistsException;

    /**
     * @throws NameAlreadyExistsException if the name is already in use
     * @throws TableStateException if the table elements is not empty
     * */
    <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws NameAlreadyExistsException, TableStateException;

    // Getters

    @NotNull Table getTable();

    <E> @NotNull Optional<? extends Column<E>> get(@NotNull String name, @NotNull DataType<E> dataType);

    @Override
    @Unmodifiable @NotNull Collection<? extends @NotNull Column<?>> toCollection();
}

package ghostface.dev.table.column;

import ghostface.dev.datatype.DataType;
import ghostface.dev.exception.table.ColumnException;
import ghostface.dev.exception.table.ColumnKeyException;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Columns {

    @NotNull Table getTable();

    <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnKeyException, ColumnException;

    <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @UnknownNullability E value, boolean isNullable) throws ColumnException;

    boolean delete(@NotNull String name);

    @NotNull Optional<? extends Column<?>> get(@NotNull String name);

    @Unmodifiable @NotNull Collection<? extends Column<?>> toCollection();

}

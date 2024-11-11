package ghostface.dev.storage.table.column;


import ghostface.dev.datatype.DataType;
import ghostface.dev.storage.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Columns {

    @NotNull Table getTable();

    <E> @NotNull Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType);

    <E> @NotNull Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, boolean isNullable);

    @NotNull Column<?> delete(@NotNull String name);

    @Unmodifiable @NotNull Collection<Column<?>> toCollection();

}

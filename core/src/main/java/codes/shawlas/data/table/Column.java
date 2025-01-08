package codes.shawlas.data.table;

import codes.shawlas.data.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Column<T> extends Comparable<@NotNull Column<?>> {

    @NotNull Table getTable();

    @NotNull String getName();

    @NotNull DataType<T> getDataType();

    @Nullable T getDefault();

    boolean isKey();

    boolean isNullable();

    void setName(@NotNull String name);

}

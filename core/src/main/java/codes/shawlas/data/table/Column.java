package codes.shawlas.data.table;

import codes.shawlas.data.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface Column<T> extends Serializable, Comparable<@NotNull Column<?>> {

    @NotNull Table getTable();

    @NotNull String getId();

    @NotNull String getName();

    @NotNull DataType<T> getDataType();

    @Nullable T getDefault();

    boolean isKey();

    boolean isNullable();

    void setName(@NotNull String name);

}

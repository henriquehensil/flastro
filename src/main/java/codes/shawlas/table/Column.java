package codes.shawlas.table;

import codes.shawlas.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface Column<T> {

    @NotNull Table getTable();

    @NotNull String getId();

    @NotNull String getName();

    void setName(@NotNull String name);

    @NotNull DataType<T> getDataType();

    @UnknownNullability T getDefault();

    boolean isNullable();

    boolean isKey();

}

package ghostface.dev.table.column;

import ghostface.dev.DataType;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface Column<T> {

    boolean isKey();

    boolean isNullable();

    // Getters

    @NotNull Table getTable();

    @NotNull String getName();
    void setName(@NotNull String name);

    @NotNull DataType<T> getDataType();

    @UnknownNullability T getDefault();
}

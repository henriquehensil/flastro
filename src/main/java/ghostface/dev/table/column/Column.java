package ghostface.dev.table.column;

import ghostface.dev.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface Column<T> {

    @NotNull String getId();

    @NotNull String getName();

    boolean isNullable();

    boolean isKey();

    void setName(@NotNull String name);

    @NotNull DataType<T> getDataType();

    @UnknownNullability T getDefault();
}

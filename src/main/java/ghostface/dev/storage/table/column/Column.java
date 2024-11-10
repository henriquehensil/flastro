package ghostface.dev.storage.table.column;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface Column<T> {

    @NotNull String getName();

    boolean isKey();

    boolean isNullable();

    @UnknownNullability T getDefaultValue();

    @NotNull DataType<T> getDataType();

}

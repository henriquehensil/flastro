package ghostface.dev.storage.table.column;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface Column<T> {

    @NotNull String getName();

    @NotNull DataType<T> getDataType();

    boolean isKey();

    boolean isNullable();

}

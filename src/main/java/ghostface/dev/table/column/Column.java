package ghostface.dev.table.column;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

public interface Column<T> {

    @NotNull String getName();

    @NotNull DataType<T> getDataType();

}

package ghostface.dev.storage.table;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

public interface Column<T> {

    @NotNull String getName();

    boolean isKey();

    boolean isNullable();

    @NotNull DataType<T> getDataType();

}

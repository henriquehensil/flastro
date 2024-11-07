package ghostface.dev.storage.table.column;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

public interface Column<T> {

    @NotNull String getName();

    boolean isUnique();

    boolean isNullable();

    @NotNull DataType<T> getDataType();

}

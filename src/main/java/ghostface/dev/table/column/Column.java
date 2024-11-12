package ghostface.dev.table.column;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

public interface Column<T> {

    @NotNull String getName();

    @NotNull ColumnUtils<T> getUtils();

    @NotNull DataType<T> getDataType();

    boolean isKey();

    boolean isNullable();

    void setName(@NotNull String name);

}

package ghostface.dev.table;

import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Data<T> {

    @NotNull Column<T> getColumn();

    @Nullable T getValue();
}

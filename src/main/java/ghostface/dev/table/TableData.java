package ghostface.dev.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TableData<T> {

    @NotNull Column<T> getColumn();

    @Nullable T getValue();
}

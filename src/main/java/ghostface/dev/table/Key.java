package ghostface.dev.table;

import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;

public interface Key<T> {

    @NotNull Column<T> getColumn();

    @NotNull T getValue();
}

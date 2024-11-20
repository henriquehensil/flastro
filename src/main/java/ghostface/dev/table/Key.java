package ghostface.dev.table;

import ghostface.dev.table.column.KeyColumn;
import org.jetbrains.annotations.NotNull;

public interface Key<T> {

    @NotNull KeyColumn<T> getColumn();

    @NotNull T getValue();
}

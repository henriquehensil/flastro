package ghostface.dev.table.column;

import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;

public interface ColumnUtils<T> {

    @NotNull T getDefaultValue();

    @NotNull T generateValue(@NotNull Table table);

}

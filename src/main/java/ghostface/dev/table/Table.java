package ghostface.dev.table;

import ghostface.dev.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Table {

    @NotNull Columns getColumns();

    @NotNull String getName();

    @NotNull Object getData(int row);

    boolean equals(@Nullable Object o);

    int hashCode();
}

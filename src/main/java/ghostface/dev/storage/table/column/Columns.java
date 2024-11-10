package ghostface.dev.storage.table.column;


import ghostface.dev.storage.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface Columns extends Iterable<Column<?>> {

    @NotNull Table getTable();

    @NotNull Column<?> create(@NotNull String name);

    @Unmodifiable @NotNull Collection<Column<?>> toCollection();

}

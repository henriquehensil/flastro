package ghostface.dev.table;

import ghostface.dev.storage.TableStorage;
import ghostface.dev.table.column.Columns;
import ghostface.dev.table.data.Elements;
import org.jetbrains.annotations.NotNull;

public interface Table {

    @NotNull TableStorage getTableStorage();

    @NotNull Elements getElements();

    @NotNull Columns getColumns();
}

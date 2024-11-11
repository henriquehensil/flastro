package ghostface.dev.storage.table;

import ghostface.dev.storage.table.column.Columns;
import ghostface.dev.storage.table.data.Datas;
import org.jetbrains.annotations.NotNull;

public interface Table {

    @NotNull String getName();

    @NotNull Datas getElements();

    @NotNull Columns getColumns();

}

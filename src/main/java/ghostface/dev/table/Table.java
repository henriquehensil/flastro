package ghostface.dev.table;

import ghostface.dev.database.Database;
import ghostface.dev.table.column.Columns;
import ghostface.dev.table.data.Datas;
import org.jetbrains.annotations.NotNull;

public interface Table {

    @NotNull Database getDatabase();

    @NotNull Datas getElements();

    @NotNull Columns getColumns();
}

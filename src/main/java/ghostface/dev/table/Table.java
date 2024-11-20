package ghostface.dev.table;

import ghostface.dev.database.Database;
import ghostface.dev.table.column.Columns;
import ghostface.dev.table.data.Elements;
import org.jetbrains.annotations.NotNull;

public interface Table {

    @NotNull Database getDatabase();

    @NotNull Elements getElements();

    @NotNull Columns getColumns();
}

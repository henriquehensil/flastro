package ghostface.dev.table;

import ghostface.dev.Data;
import ghostface.dev.exception.TableException;
import ghostface.dev.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface Table {

    @NotNull String getName();

    @NotNull Columns getColumns();

    @NotNull Data getData(int row);

    boolean add(@NotNull Data data) throws TableException;

    boolean remove(@NotNull Data data) throws TableException;

    @Unmodifiable @NotNull List<Data> getElements();

}

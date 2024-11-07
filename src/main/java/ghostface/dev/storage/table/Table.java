package ghostface.dev.storage.table;

import ghostface.dev.exception.TableException;
import ghostface.dev.storage.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;

public interface Table {

    @NotNull String getName();

    @NotNull Columns getColumns();

    @NotNull Data getData(int row);

    @NotNull Data getData(@NotNull UUID uuid);

    boolean add(@NotNull Data data) throws TableException;

    boolean remove(@NotNull Data data) throws TableException;

    @Unmodifiable @NotNull Collection<Data> getElements();

}

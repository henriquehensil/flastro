package ghostface.dev;

import ghostface.dev.exception.ColumnException;
import ghostface.dev.exception.DataException;
import ghostface.dev.storage.FileStorage;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public interface Data {

    @NotNull Table getTable();

    @NotNull FileStorage getFileStorage();

    <E> @UnknownNullability E get(@NotNull Column<E> column) throws IllegalArgumentException;

    <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException, DataException;

}

package ghostface.dev.storage.table;

import ghostface.dev.exception.ColumnException;
import ghostface.dev.exception.DataException;
import ghostface.dev.storage.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.Serializable;
import java.util.UUID;

public interface Data extends Serializable {

    @NotNull UUID getUniqueId();

    @NotNull Table getTable();

    <E> @UnknownNullability E get(@NotNull Column<E> column) throws IllegalArgumentException;

    <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException, DataException;

}

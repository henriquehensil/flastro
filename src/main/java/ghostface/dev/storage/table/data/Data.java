package ghostface.dev.storage.table.data;

import ghostface.dev.exception.table.ColumnException;
import ghostface.dev.exception.DataTypeException;
import ghostface.dev.exception.NonCorrespondingException;
import ghostface.dev.storage.table.Table;
import ghostface.dev.storage.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.Serializable;

public interface Data extends Serializable {

    @NotNull String getUniqueId();

    @NotNull Table getTable();

    <E> @UnknownNullability E get(@NotNull Column<E> column) throws NonCorrespondingException;

    <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException, NonCorrespondingException;

}

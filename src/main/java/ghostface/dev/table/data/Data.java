package ghostface.dev.table.data;

import ghostface.dev.exception.column.ColumnException;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Data {

    @NotNull Table getTable();

    <E> @NotNull Optional<E> get(@NotNull Column<E> column);

    /**
     * @throws ColumnException if the value is null and the column is either non-nullable or a key.
     * */
    <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException;

    @Unmodifiable @NotNull Collection<Object> toCollection();
}

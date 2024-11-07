package ghostface.dev.providers;

import ghostface.dev.exception.DataException;
import ghostface.dev.storage.table.Data;
import ghostface.dev.exception.ColumnException;
import ghostface.dev.storage.file.FileStorage;
import ghostface.dev.storage.table.Table;
import ghostface.dev.storage.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractData implements Data {

    private final @NotNull Table table;
    private final @NotNull UUID uuid;

    protected AbstractData(@NotNull Table table, @NotNull UUID uuid) {
        this.table = table;
        this.uuid = uuid;
    }

    @Override
    public final @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public final @NotNull Table getTable() {
        return table;
    }

    @Override
    public abstract <E> @UnknownNullability E get(@NotNull Column<E> column) throws IllegalArgumentException;

    @Override
    public abstract <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException, DataException;

    @Override
    public final boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull AbstractData that = (AbstractData) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(uuid);
    }
}

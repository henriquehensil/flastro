package ghostface.dev.providers;

import ghostface.dev.storage.table.Data;
import ghostface.dev.storage.table.Table;
import ghostface.dev.storage.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractTable implements Table {

    private final @NotNull String name;
    private final @NotNull Columns columns;

    protected AbstractTable(@NotNull String name, @NotNull Columns columns) {
        if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
        this.columns = columns;
    }

    @Override
    public final @NotNull String getName() {
        return name;
    }

    @Override
    public final @NotNull Columns getColumns() {
        return columns;
    }

    @Override
    public abstract @NotNull Data getData(int row);

    @Override
    public abstract boolean add(@NotNull Data data);

    @Override
    public abstract boolean remove(@NotNull Data data);

    @Override
    public abstract @Unmodifiable @NotNull Collection<Data> getElements();

    @Override
    public final boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull AbstractTable that = (AbstractTable) object;
        return this.name.equalsIgnoreCase(that.name.toLowerCase());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(name.toLowerCase());
    }
}

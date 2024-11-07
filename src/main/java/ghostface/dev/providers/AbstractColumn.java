package ghostface.dev.providers;

import ghostface.dev.datatype.DataType;
import ghostface.dev.storage.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractColumn<T> implements Column<T> {

    private final @NotNull String name;

    public AbstractColumn(@NotNull String name) {
        if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

    @Override
    public final @NotNull String getName() {
        return name;
    }

    @Override
    public abstract @NotNull DataType<T> getDataType();

    @Override
    public final boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull AbstractColumn<?> that = (AbstractColumn<?>) object;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(name.toLowerCase());
    }
}

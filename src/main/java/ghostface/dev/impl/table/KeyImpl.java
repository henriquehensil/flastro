package ghostface.dev.impl.table;

import ghostface.dev.table.Key;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class KeyImpl<T> implements Key<T> {

    private final @NotNull Column<T> column;
    private final @NotNull T value;

    public KeyImpl(@NotNull Column<T> column, @NotNull T value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public @NotNull Column<T> getColumn() {
        return column;
    }

    @Override
    public @NotNull T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        KeyImpl<?> key = (KeyImpl<?>) object;
        return Objects.equals(column, key.column);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(column);
    }
}
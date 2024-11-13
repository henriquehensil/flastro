package ghostface.dev.providers;

import ghostface.dev.table.Key;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractKey<T> implements Key<T> {

    private final @NotNull T value;
    private final @NotNull Column<T> column;

    public AbstractKey(@NotNull T value, @NotNull Column<T> column) {
        this.value = value;
        this.column = column;
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
    public @NotNull String toString() {
        return "AbstractKey{" +
                "value=" + value +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @NotNull AbstractKey<?> that = (AbstractKey<?>) o;
        return Objects.equals(value, that.value) && Objects.equals(column, that.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, column);
    }
}

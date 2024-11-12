package ghostface.dev.providers;

import ghostface.dev.exception.table.ColumnException;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.data.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class AbstractData implements Data {

    protected final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new LinkedHashMap<>();

    private final @NotNull Table table;

    public AbstractData(@NotNull Table table) {
        this.table = table;
    }

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> @UnknownNullability E get(@NotNull Column<E> column) throws IllegalArgumentException {
        if (!values.containsKey(column)) {
            throw new IllegalArgumentException("Column does not exists");
        } else {
            return (E) values.get(column);
        }
    }

    @Override
    public <E> void set(@NotNull Column<E> column, @Nullable E value) throws ColumnException, IllegalArgumentException {
        if (!values.containsKey(column)) {
            throw new IllegalArgumentException("Column does not exists");
        } else if (column.isKey()) {
            throw new ColumnException("Cannot set an Column Key");
        } else if (value == null && !column.isNullable()) {
            throw new ColumnException("Column is not nullable");
        } else {
            values.replace(column, value);
        }
    }

    @Override
    public @Unmodifiable @NotNull Collection<@Nullable Object> getValues() {
        return Collections.unmodifiableCollection(values.values());
    }

    @Override
    public @NotNull String toString() {
        return "AbstractData{" +
                "values=" + values +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull AbstractData that = (AbstractData) object;
        return Objects.deepEquals(getValues(), that.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValues());
    }
}

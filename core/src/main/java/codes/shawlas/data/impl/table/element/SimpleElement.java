package codes.shawlas.data.impl.table.element;

import codes.shawlas.data.exception.ColumnException;
import codes.shawlas.data.exception.ColumnException.*;
import codes.shawlas.data.impl.table.TableLock;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Element;
import codes.shawlas.data.table.EntryData;
import codes.shawlas.data.table.Table;
import codes.shawlas.data.impl.table.element.SimpleElements.AutoIncrement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

// todo: rework lock
final class SimpleElement implements Element {

    private final @NotNull TableLock lock;

    private final @NotNull AutoIncrement increment;
    private final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new TreeMap<>(Comparator.naturalOrder());
    private final @NotNull Set<@NotNull EntryData<?>> data = new HashSet<>();

    private final @NotNull Table table;

    SimpleElement(@NotNull TableLock lock, @NotNull Table table, @NotNull AutoIncrement increment) {
        if (table.getColumns().getAll().isEmpty()) {
            throw new IllegalStateException("No columns");
        } else for (@NotNull Column<?> column : table.getColumns().getAll()) {
            values.put(column, column.getDefault());
        }

        this.lock = lock;
        this.table = table;
        this.increment = increment;
    }

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) int getIndex() {
        return increment.getActual();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> @Nullable E getValue(@NotNull Column<E> column) throws ColumnException.InvalidColumnException {
        if (!values.containsKey(column)) {
            throw new ColumnException.InvalidColumnException(column);
        } else synchronized (lock) {
            return (E) values.get(column);
        }
    }

    /**
     * @throws InvalidColumnException if {@code column} not exists
     * @throws ColumnTypeException if {@code column} is key or {@code value} is null but the column is not nullable
     * */
    @Override
    public <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws ColumnException.InvalidColumnException, ColumnException.ColumnTypeException {
        if (!values.containsKey(column)) {
            throw new ColumnException.InvalidColumnException(column);
        } else if (column.isKey() || !column.isNullable() && value == null) {
            throw new ColumnException.ColumnTypeException(column, value);
        } else synchronized (lock) {
            values.replace(column, value);
        }
    }

    @NotNull Map<@NotNull Column<?>, @Nullable Object> getMap() {
        return values;
    }

    @NotNull AutoIncrement getIncrement() {
        return increment;
    }

    public @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getKeyData() {
        return getData().stream().filter(data -> data.getColumn().isKey()).collect(Collectors.toSet());
    }

    public boolean keysContainsValue(@Nullable Object value) {
        return getKeyData().stream().anyMatch(data -> Objects.equals(data.getValue(), value));
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getData() {
        return data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull SimpleElement that = (SimpleElement) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }
}

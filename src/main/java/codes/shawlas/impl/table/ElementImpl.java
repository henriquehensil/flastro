package codes.shawlas.impl.table;

import codes.shawlas.exception.column.ColumnException;
import codes.shawlas.exception.column.InvalidColumnException;
import codes.shawlas.exception.table.TableStateException;
import codes.shawlas.table.Column;
import codes.shawlas.table.Element;
import codes.shawlas.table.TableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public final class ElementImpl implements Element {

    final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new TreeMap<>(ColumnsImpl.getComparator());

    private final @NotNull Object lock = new Object();
    private final @NotNull Set<@NotNull TableData<?>> data = new LinkedHashSet<>();
    private final @NotNull UUID id;
    private final @NotNull TableImpl table;

    @SuppressWarnings("unchecked")
    public ElementImpl(@NotNull UUID id, @NotNull TableImpl table) {
        this.id = id;
        this.table = table;

        if (table.getColumns().toCollection().isEmpty()) {
            throw new TableStateException("No columns");
        } else for (@NotNull ColumnImpl<?> column : table.getColumns()) {
            values.put(column, column.getDefault());
            data.add(new TableData<>((Column<Object>) column, column.getDefault()));
        }
    }

    public @Unmodifiable @NotNull Set<@NotNull TableData<?>> getKeyData() {
        return getData().stream().filter(data -> data.getColumn().isKey()).collect(Collectors.toSet());
    }

    public boolean keysContainsValue(@Nullable Object value) {
        return getKeyData().stream().anyMatch(tableData -> Objects.equals(tableData.getValue(), value));
    }

    @Override
    public @NotNull String getId() {
        return id.toString();
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> @Nullable E get(@NotNull Column<E> column) throws InvalidColumnException {
        if (!values.containsKey(column)) {
            throw new InvalidColumnException("This Column does not belong here");
        } else {
            return (E) values.get(column);
        }
    }

    @Override
    public <E> void set(@NotNull Column<E> column, @Nullable E value) throws InvalidColumnException, ColumnException {
        if (!values.containsKey(column)) {
            throw new InvalidColumnException("Column does not belong to this table");
        } else if (column.isKey()) {
            throw new ColumnException("Column key value is immutable");
        } else if (!column.isNullable() && value == null) {
            throw new ColumnException("Column key value not accept null value");
        } else synchronized (lock) {
            values.replace(column, value);
        }
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull TableData<?>> getData() {
        return data;
    }

    // Native

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Element) {
            final @NotNull Element element = (Element) o;
            return Objects.equals(this.getId(), element.getId()) && Objects.equals(table, element.getTable());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), table);
    }
}

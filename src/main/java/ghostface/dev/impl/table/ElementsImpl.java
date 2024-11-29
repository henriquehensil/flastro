package ghostface.dev.impl.table;

import ghostface.dev.exception.column.ColumnException;
import ghostface.dev.exception.column.DuplicatedColumnException;
import ghostface.dev.exception.key.DuplicatedKeyValueException;
import ghostface.dev.exception.key.MissingKeyException;
import ghostface.dev.exception.table.TableStateException;
import ghostface.dev.table.Key;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.data.Data;
import ghostface.dev.table.data.Elements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class ElementsImpl implements Elements {

    private final @NotNull Map<@NotNull Integer, @NotNull DataImpl> rows = new LinkedHashMap<>();
    private final @NotNull TableImpl table;

    private final @NotNull AtomicInteger increment = new AtomicInteger(0);
    private final @NotNull Object lock = new Object();

    ElementsImpl(@NotNull TableImpl table) {
        this.table = table;
    }

    public void load() {
        @NotNull List<@NotNull DataImpl> dataList = rows.values()
                .stream()
                .filter(data -> data.getValues().keySet().size() != table.getColumns().size())
                .collect(Collectors.toList());

        if (dataList.isEmpty()) {
            return;
        }

        for (@NotNull DataImpl data : dataList) {
            @NotNull List<@NotNull Column<?>> columns = table.getColumns()
                    .stream()
                    .filter(column -> !data.getValues().containsKey(column))
                    .collect(Collectors.toList());

            columns.forEach(column -> data.getValues().put(column, column.getDefault()));
        }
    }

    @Override
    public @NotNull Data create(@NotNull Key<?> @NotNull ... keys) throws MissingKeyException, DuplicatedKeyValueException, DuplicatedColumnException, TableStateException {
        if (getTable().getColumns().toCollection().isEmpty()) {
            throw new TableStateException("It is not possible to create new data because there is no column");
        }

        @NotNull Set<Column<?>> keyColumns = getTable().getColumns().getKeys();

        @NotNull Set<Key<?>> keyMatches = Arrays
                .stream(keys)
                .filter(key -> keyColumns.contains(key.getColumn()))
                .collect(Collectors.toSet());

        @NotNull Set<Column<?>> ColumnsMatches = keyMatches
                .stream()
                .map(Key::getColumn)
                .collect(Collectors.toSet());

        if (keyColumns.size() != ColumnsMatches.size() || !ColumnsMatches.containsAll(keyColumns)) {
            throw new MissingKeyException("Key is missing");
        }

        @NotNull DataImpl data = new DataImpl();

        if (!keyColumns.isEmpty()) {
            for (@NotNull Key<?> key : keyMatches) {
                if (containsValue(rows.values(), key.getValue())) {
                    throw new DuplicatedKeyValueException("Key value already in use: " + key.getValue());
                } else {
                    data.getValues().put(key.getColumn(), key.getValue());
                }
            }
        }

        for (@NotNull Column<?> column : getTable().getColumns().getWithoutKeys()) {
            data.getValues().put(column, column.getDefault());
        }

        synchronized (lock) {
            rows.put(increment.incrementAndGet(), data);
        }

        return data;
    }

    private boolean containsValue(@NotNull Collection<DataImpl> datas, @Nullable Object value) {
        return datas.stream().anyMatch(data -> data.containsValue(value));
    }

    @Override
    public synchronized boolean remove(int index) {
        return rows.remove(index) != null;
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    public @NotNull Optional<Data> get(int index) {
        return Optional.ofNullable(rows.get(index));
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull Data> toCollection() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(rows.values()));
    }

    public int size() {
        return toCollection().size();
    }

    // Classes

    private final class DataImpl implements Data {

        private final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new HashMap<>();

        @Override
        public @NotNull ElementsImpl getElements() {
            return ElementsImpl.this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <E> @Nullable E get(@NotNull Column<E> column) throws IllegalArgumentException {
            getElements().load();

            if (!values.containsKey(column)) {
                throw new IllegalArgumentException("This column does not exist: " + column);
            } else {
                return (E) values.get(column);
            }
        }

        @Override
        public <E> void set(@NotNull Column<E> column, @Nullable E value) throws IllegalArgumentException, ColumnException {
            getElements().load();

            if (!values.containsKey(column)) {
                throw new IllegalArgumentException("This column does not exist: " + column);
            } else if (column.isKey()) {
                throw new ColumnException("Cannot change the value of a Key Column");
            } else if (value == null && !column.isNullable()) {
                throw new ColumnException("Cannot assign the null value in the nullable Column");
            } else synchronized (lock) {
                values.replace(column, value);
            }
        }

        @Override
        public @Unmodifiable @NotNull Collection<Object> toCollection() {
            return Collections.unmodifiableCollection(values.values());
        }

        private @NotNull Map<@NotNull Column<?>, @Nullable Object> getValues() {
            return values;
        }

        private boolean containsValue(@Nullable Object value) {
            return toCollection().stream().anyMatch(obj -> {
                if (value instanceof String && obj instanceof String) {
                    return ((String) obj).equalsIgnoreCase((String) value);
                } else {
                    return Objects.equals(obj, value);
                }
            });
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull DataImpl data = (DataImpl) object;
            return Objects.equals(values, data.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }
    }
}
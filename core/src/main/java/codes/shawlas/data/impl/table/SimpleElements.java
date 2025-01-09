package codes.shawlas.data.impl.table;

import codes.shawlas.data.exception.column.*;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Element;
import codes.shawlas.data.table.EntryData;
import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// todo: remove Element#getIncrement and elements#update
final class SimpleElements implements Table.Elements {

    private final @NotNull Object lock;
    private final @NotNull SimpleTable table;

    private final @NotNull AtomicInteger count = new AtomicInteger(0);
    private final @NotNull Map<@NotNull AutoIncrement, @NotNull SimpleElement> rows = new TreeMap<>();

    SimpleElements(@NotNull SimpleTable table, @NotNull Object lock) {
        this.lock = lock;
        this.table = table;
    }

    @Override
    public @NotNull SimpleTable getTable() {
        return table;
    }

    @Override
    public @NotNull Element create(@NotNull EntryData<?> @NotNull ... entryData)
            throws DuplicatedColumnException, NoColumnsException, MissingKeyColumnException, InvalidColumnException, DuplicatedKeyValueException
    {
        final @NotNull Set<@NotNull Column<?>> thatColumns = Arrays.stream(entryData).map(EntryData::getColumn).collect(Collectors.toSet());

        if (thatColumns.size() != entryData.length) {
            throw new DuplicatedColumnException("Cannot accept duplicated column: " + Arrays.toString(entryData));
        }

        synchronized (lock) {
            if (table.getColumns().getAll().isEmpty()) {
                throw new NoColumnsException("No columns");
            }

            @NotNull Collection<? extends @NotNull Column<?>> columns = getTable().getColumns().getAll();

            if (getKeys(thatColumns).size() != getKeys(columns).size()) {
                throw new MissingKeyColumnException("Some key is missing");
            }

            final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new HashMap<>();

            for (@NotNull EntryData<?> data : entryData) {
                if (!columns.contains(data.getColumn())) {
                    throw new InvalidColumnException(data.getColumn());
                } else if (data.getColumn().isKey() && containsValue(data.getValue())) {
                    throw new DuplicatedKeyValueException("The key value is already in use: " + data.getValue());
                } else {
                    values.put(data.getColumn(), data.getValue());
                }
            }

            @NotNull AutoIncrement increment = new AutoIncrement(count.incrementAndGet());
            @NotNull SimpleElement element = new SimpleElement(increment);
            element.getValues().putAll(values);

            this.rows.put(increment, element);
            return element;
        }
    }

    /**
     * Upgrade each elements column
     * */
    void upgrade(@NotNull Column<?> column) {
        if (!getTable().getColumns().equals(column.getTable().getColumns())) {
            throw new RuntimeException("Cannot upgrade with this specific Column");
        } else for (@NotNull SimpleElement element : this.rows.values()) {
            element.getValues().compute(column, (key, value) -> value == null ? column.getDefault() : null);
        }
    }

    private boolean containsValue(@Nullable Object value) {
        return this.rows.values().stream().anyMatch(e -> e.keysContainsValue(value));
    }

    /**
     * Filters by key columns
     * */
    private @NotNull Set<Column<?>> getKeys(@NotNull Collection<? extends @NotNull Column<?>> columns) {
        return columns.stream().filter(Column::isKey).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Optional<@NotNull Element> get(int row) {
        if (row <= 0) return Optional.empty();

        synchronized (lock) {
            @Nullable AutoIncrement key = getKey(row);
            return key == null ? Optional.empty() : Optional.of(this.rows.get(key));
        }
    }

    @Override
    public boolean delete(int row) {
        if (row <= 0) return false;

        synchronized (lock) {
            @Nullable Element element = get(row).orElse(null);
            if (element == null && getKey(row) == null) return false;

            this.rows.remove(getKey(row));
            this.count.decrementAndGet();

            @NotNull Set<@NotNull AutoIncrement> rowToModify = this.rows.keySet()
                    .stream()
                    .filter(key -> key.actual > row)
                    .collect(Collectors.toSet());

            for (@NotNull AutoIncrement increment : rowToModify) {
                increment.decrement();
                this.rows.get(increment).decrement();
            }

            return true;
        }
    }

    private @Nullable AutoIncrement getKey(int row) {
        return rows.keySet().stream().filter(key -> key.getActual() == row).findFirst().orElse(null);
    }

    @Override
    public @Unmodifiable @NotNull Collection<@NotNull Element> getAll() {
        return Collections.unmodifiableCollection((rows.values()));
    }

    // Classes

    private final class SimpleElement implements Element {

        private int index;
        private final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new TreeMap<>(Comparator.naturalOrder());

        SimpleElement(@NotNull AutoIncrement increment) {
            for (@NotNull Column<?> column : table.getColumns().getAll()) {
                values.put(column, column.getDefault());
            }

            this.index = increment.getActual();
        }

        private @NotNull Map<@NotNull Column<?>, @Nullable Object> getValues() {
            return values;
        }

        @Override
        public @NotNull SimpleTable getTable() {
            return SimpleElements.this.table;
        }

        @Override
        public @Range(from = 0, to = Long.MAX_VALUE) int getIndex() {
            return index;
        }

        private void decrement() {
            index--;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <E> @Nullable E getValue(@NotNull Column<E> column) throws InvalidColumnException {
            synchronized (lock) {
                if (!values.containsKey(column)) {
                    throw new InvalidColumnException(column);
                } else {
                    return (E) values.get(column);
                }
            }
        }

        /**
         * @throws InvalidColumnException if {@code column} not exists
         * @throws ColumnTypeException if {@code column} is key or {@code value} is null but the column is not nullable
         * */
        @Override
        public <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws InvalidColumnException, ColumnTypeException {
            if (column.isKey() || !column.isNullable() && value == null) {
                throw new ColumnTypeException(column, value);
            } else synchronized (lock) {
                if (!values.containsKey(column)) {
                    throw new InvalidColumnException(column);
                }
                values.replace(column, value);
            }
        }

        public @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getKeyData() {
            return getData().stream().filter(data -> data.getColumn().isKey()).collect(Collectors.toSet());
        }

        public boolean keysContainsValue(@Nullable Object value) {
            return getKeyData().stream().anyMatch(data -> Objects.equals(data.getValue(), value));
        }

        @Override
        public @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getData() {
            @NotNull Set<@NotNull EntryData<?>> data = new HashSet<>();
            // noinspection unchecked
            values.forEach((key, value) -> data.add(new EntryData<>((Column<Object>) key, value)));
            return Collections.unmodifiableSet(data);
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

    // AutoIncrement

    private static final class AutoIncrement implements Comparable<@NotNull AutoIncrement> {

        private int actual;

        private AutoIncrement(int index) {
            if (index <= 0) throw new IllegalStateException("Invalid index");
            this.actual = index;
        }

        public int getActual() {
            return actual;
        }

        public void decrement() {
            if (getActual() <= 0) throw new IllegalStateException("Cannot decrement");
            actual--;
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(actual);
        }

        @Override
        public int compareTo(@NotNull AutoIncrement o) {
            return Integer.compare(getActual(), o.getActual());
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final @NotNull AutoIncrement that = (AutoIncrement) o;
            return getActual() == that.getActual();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getActual());
        }
    }
}

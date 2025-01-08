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
    public @NotNull Element create(@NotNull EntryData<?> @NotNull ... entryData) throws ColumnException {
        final @NotNull Set<@NotNull Column<?>> thatColumns = Arrays.stream(entryData).map(EntryData::getColumn).collect(Collectors.toSet());

        if (thatColumns.size() != entryData.length) {
            throw new DuplicatedColumnException("Cannot accept duplicated column: " + Arrays.toString(entryData));
        }

        synchronized (lock) {
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

            @NotNull SimpleElement element = new SimpleElement(new AutoIncrement(this.count.incrementAndGet()));
            element.getValues().putAll(values);

            this.rows.put(element.getIncrement(), element);
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
    public @NotNull Optional<@NotNull Element> get(int index) {
        return Optional.ofNullable(this.rows.get(new AutoIncrement(index)));
    }

    @Override
    public boolean delete(int index) {
        if (index <= 0) return false;

        synchronized (lock) {
            @Nullable Element element = get(index).orElse(null);
            if (element == null && getKey(index) == null) return false;

            this.rows.remove(getKey(index));
            this.count.decrementAndGet();

            @NotNull Set<@NotNull AutoIncrement> rowToModify = this.rows.keySet()
                    .stream()
                    .filter(row -> row.needDecrement(this.count.get()))
                    .collect(Collectors.toSet());

            @NotNull Collection<@NotNull SimpleElement> elementsToDecrement = this.rows.values()
                    .stream()
                    .filter(e -> e.getIndex() == this.count.get())
                    .collect(Collectors.toSet());

            rowToModify.forEach(AutoIncrement::decrement);
            elementsToDecrement.forEach(e -> e.getIncrement().decrement());

            return true;
        }
    }

    private @Nullable AutoIncrement getKey(int index) {
        return rows.keySet().stream().filter(row -> row.getActual() == index).findFirst().orElse(null);
    }

    @Override
    public @Unmodifiable @NotNull Collection<@NotNull Element> getAll() {
        return Collections.unmodifiableCollection((rows.values()));
    }

    // Classes

    private final class SimpleElement implements Element {

        private final @NotNull AutoIncrement increment;
        private final @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new TreeMap<>(Comparator.naturalOrder());
        private final @NotNull Set<@NotNull EntryData<?>> data = new HashSet<>();

        SimpleElement(@NotNull AutoIncrement increment) {
            if (table.getColumns().getAll().isEmpty()) {
                throw new IllegalStateException("No columns");
            } else for (@NotNull Column<?> column : table.getColumns().getAll()) {
                values.put(column, column.getDefault());
            }

            this.increment = increment;
        }

        private @NotNull AutoIncrement getIncrement() {
            return increment;
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
            return increment.getActual();
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

    // AutoIncrement

    private static final class AutoIncrement implements Comparable<@NotNull Integer> {

        private int actual;

        private AutoIncrement(int index) {
            if (index <= 0) throw new IllegalStateException("Invalid index");
            this.actual = index;
        }

        public int getActual() {
            return actual;
        }

        public boolean needDecrement(int index) {
            return index > 0 && index < getActual();
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
        public int compareTo(@NotNull Integer index) {
            return Integer.compare(getActual(), index);
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

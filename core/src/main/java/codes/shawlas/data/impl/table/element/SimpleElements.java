package codes.shawlas.data.impl.table.element;

import codes.shawlas.data.exception.column.*;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Element;
import codes.shawlas.data.table.EntryData;
import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class SimpleElements implements Table.Elements {

    private final @NotNull Object lock;

    private final @NotNull AtomicInteger count = new AtomicInteger(0);
    private final @NotNull Map<@NotNull AutoIncrement, @NotNull SimpleElement> rows = new TreeMap<>();
    private final @NotNull Table table;

    private SimpleElements(@NotNull Object lock, @NotNull Table table) {
        this.lock = lock;
        this.table = table;
    }

    @Override
    public @NotNull Table getTable() {
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

            @NotNull SimpleElement element = new SimpleElement(lock, table, new AutoIncrement(count.incrementAndGet()));
            element.getMap().putAll(values);

            rows.put(element.getIncrement(), element);
            return element;
        }
    }

    private boolean containsValue(@Nullable Object value) {
        return getAll().stream().anyMatch(e -> ((SimpleElement) e).keysContainsValue(value));
    }

    /**
     * Filters by key columns
     * */
    private @NotNull Set<Column<?>> getKeys(@NotNull Collection<? extends @NotNull Column<?>> columns) {
        return columns.stream().filter(Column::isKey).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Optional<@NotNull Element> get(int index) {
        return Optional.ofNullable(rows.get(new AutoIncrement(index)));
    }

    @Override
    public boolean delete(int index) {
        if (index <= 0) return false;

        synchronized (lock) {
            @Nullable Element element = get(index).orElse(null);
            if (element == null && getKey(index) == null) return false;

            rows.remove(getKey(index));
            count.decrementAndGet();

            @NotNull Set<@NotNull AutoIncrement> rowToModify = this.rows.keySet().stream().filter(row -> row.needDecrement(count.get())).collect(Collectors.toSet());
            rowToModify.forEach(AutoIncrement::decrement);

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

    // AutoIncrement

    static final class AutoIncrement implements Comparable<@NotNull Integer> {

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

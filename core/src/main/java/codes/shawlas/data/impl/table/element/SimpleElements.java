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
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimpleElements implements Table.Elements {

    private final @NotNull AutoIncrement autoIncrement = new AutoIncrement(0);

    private final @NotNull Map<@NotNull AutoIncrement, @NotNull SimpleElement> rows = new TreeMap<>(Comparator.naturalOrder());

    private final @NotNull Table table;
    private final @NotNull ReentrantLock lock;

    private SimpleElements(@NotNull Table table, @NotNull ReentrantLock lock) {
        if (!table.getClass().getSimpleName().equalsIgnoreCase("SimpleTable")) {
            throw new RuntimeException("This implementation elements only accept an specific table implementation");
        }

        this.lock = lock;
        this.table = table;
    }

    @NotNull ReentrantLock getLock() {
        return lock;
    }

    @Override
    public @NotNull Table getTable() {
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

        lock.lock();
        try {
            if (table.getColumns().getAll().isEmpty()) {
                throw new NoColumnsException("No columns");
            }

            @NotNull Collection<? extends @NotNull Column<?>> columns = getTable().getColumns().getAll();

            if (getKeys(thatColumns).size() < getKeys(columns).size()) {
                throw new MissingKeyColumnException("Some key is missing");
            }

            @NotNull Map<@NotNull Column<?>, @Nullable Object> values = new HashMap<>();

            for (@NotNull EntryData<?> data : entryData) {
                if (!columns.contains(data.getColumn())) {
                    throw new InvalidColumnException(data.getColumn());
                } else if (data.getColumn().isKey() && containsValue(data.getValue())) {
                    throw new DuplicatedKeyValueException("The key value is already in use: " + data.getValue());
                } else {
                    values.put(data.getColumn(), data.getValue());
                }
            }

            autoIncrement.increment();

            @NotNull SimpleElement element = new SimpleElement(this, autoIncrement.getActual());
            element.getValues().putAll(values);

            rows.put(new AutoIncrement(autoIncrement.getActual()), element);
            return element;

        } finally {
            lock.unlock();
        }
    }

    private boolean containsValue(@Nullable Object value) {
        return rows.values().stream().anyMatch(e -> e.keysContainsValue(value));
    }

    /**
     * Upgrade each elements
     * */
    void upgrade(@NotNull SimpleElement element) {
        lock.lock();
        try {
            @NotNull Function<@NotNull SimpleElement, @NotNull Set<@NotNull Column<?>>> toAdd = e -> {
                @NotNull Set<@NotNull Column<?>> updated = new HashSet<>(table.getColumns().getAll());
                updated.removeAll(element.getValues().keySet());
                return updated;
            };

            @NotNull Function< SimpleElement, @NotNull Set<@NotNull Column<?>>> toRemove = e -> {
                @NotNull Set<@NotNull Column<?>> current = new HashSet<>(e.getValues().keySet());
                current.removeAll(table.getColumns().getAll());
                return current;
            };

            @NotNull Set<@NotNull Column<?>> add = toAdd.apply(element);
            @NotNull Set<@NotNull Column<?>> remove = toRemove.apply(element);

            for (@NotNull Column<?> c : add) {
                element.getValues().put(c, c.getDefault());
            }

            for (@NotNull Column<?> c : remove) {
                element.getValues().remove(c);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Filters by key columns
     * */
    private @NotNull Set<Column<?>> getKeys(@NotNull Collection<? extends @NotNull Column<?>> columns) {
        lock.lock();
        try {
            return columns.stream().filter(Column::isKey).collect(Collectors.toSet());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NotNull Optional<@NotNull Element> get(int row) {
        if (row <= 0) return Optional.empty();

        lock.lock();
        try {
            @Nullable AutoIncrement key = getKey(row);
            if (key == null) return Optional.empty();

            @NotNull SimpleElement element = this.rows.get(key);
            upgrade(element);

            return Optional.of(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean delete(int index) {
        if (index <= 0) return false;

        lock.lock();
        try {
            if (!get(index).isPresent() || getKey(index) == null) return false;

            rows.remove(getKey(index));
            autoIncrement.decrement();

            @NotNull Set<@NotNull AutoIncrement> toModify = rows.keySet().stream().filter(i -> i.canDecrement(index)).collect(Collectors.toSet());
            if (!toModify.isEmpty()) {
                toModify.forEach((key) -> {
                    @NotNull SimpleElement element = rows.remove(key);
                    upgrade(element);

                    key.decrement();
                    element.decrement();
                    rows.put(key, element);
                });
            }

            return true;

        } finally {
            lock.unlock();
        }
    }

    private @Nullable AutoIncrement getKey(int row) {
        return rows.keySet().stream().filter(key -> key.getActual() == row).findFirst().orElse(null);
    }

    @Override
    public @Unmodifiable @NotNull Collection<@NotNull Element> getAll() {
        return Collections.unmodifiableCollection((rows.values()));
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull SimpleElements that = (SimpleElements) object;
        return Objects.equals(rows, that.rows) && Objects.equals(table.getName(), that.table.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rows);
    }

    // AutoIncrement

    private final class AutoIncrement implements Comparable<@NotNull AutoIncrement> {

        private int actual;

        private AutoIncrement(int index) {
            if (index < 0) throw new IllegalStateException("Invalid index");
            this.actual = index;
        }

        public int getActual() {
            return actual;
        }

        public void decrement() {
            if (this.getActual() == 0) throw new IllegalStateException("Cannot decrement");
            lock.lock();
            try {
                actual--;
            } finally {
                lock.unlock();
            }
        }

        public void increment() {
            actual++;
        }

        public boolean canDecrement(int index) {
            if (index < 0) throw new IllegalArgumentException("invalid index");
            return getActual() > index;
        }

//        public boolean canIncrement(@NotNull AutoIncrement increment) {
//            return this.compareTo(increment) > 0;
//        }

        // Implementations

        @Override
        public @NotNull String toString() {
            return String.valueOf(actual);
        }

        @Override
        public int compareTo(@NotNull AutoIncrement o) {
            return Integer.compare(this.getActual(), o.getActual());
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final @NotNull AutoIncrement that = (AutoIncrement) o;
            return this.getActual() == that.getActual();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getActual());
        }
    }
}
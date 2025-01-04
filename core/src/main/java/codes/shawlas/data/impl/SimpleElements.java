package codes.shawlas.data.impl;

import codes.shawlas.data.exception.ColumnException;
import codes.shawlas.data.exception.ColumnException.*;
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

public final class SimpleElements implements Table.Elements {

    private static final @NotNull AtomicInteger count = new AtomicInteger(0);

    private final @NotNull SimpleTable table;
    private final @NotNull Map<@NotNull AutoIncrement, @NotNull SimpleElement> rows = new TreeMap<>();
    private final @NotNull Object lock = new Object();

    SimpleElements(@NotNull SimpleTable table) {
        this.table = table;
    }

    @Override
    public @NotNull SimpleTable getTable() {
        return table;
    }

    @Override
    public @NotNull Element create(@NotNull EntryData<?> @NotNull ... entryData) throws ColumnException {
        @NotNull Set<@NotNull Column<?>> thatColumns = Arrays.stream(entryData).map(EntryData::getColumn).collect(Collectors.toSet());

        if (thatColumns.size() != entryData.length) {
            throw new DuplicatedColumnException("Cannot accept duplicated column: " + Arrays.toString(entryData));
        }

        synchronized (lock) {
            @NotNull Set<@NotNull Column<?>> columns = getTable().getColumns().getAll();
            @NotNull Set<@NotNull Column<?>> keys = getTable().getColumns().getKeys();

            if (thatColumns.stream().filter(Column::isKey).collect(Collectors.toSet()).size() != keys.size()) {
                throw new MissingKeyColumnException("Column key is missing");
            }

            @NotNull SimpleElement element = new SimpleElement(getTable());

            for (@NotNull EntryData<?> data : entryData) {
                if (!columns.contains(data.getColumn())) {
                    throw new InvalidColumnException(data.getColumn());
                } else if (data.getColumn().isKey() && getAll().stream().anyMatch(e -> e.keysContainsValue(data.getValue()))) {
                    throw new DuplicatedKeyValueException("The key value is already in use: " + data.getValue());
                } else {
                    element.getValues().replace(data.getColumn(), data.getValue());
                }
            }

            count.incrementAndGet();
            rows.put(new AutoIncrement(count.get()), element);

            return element;
        }
    }

    @Override
    public @NotNull Optional<@NotNull SimpleElement> get(int index) {
        return Optional.ofNullable(rows.get(new AutoIncrement(index)));
    }

    @Override
    public boolean delete(int index) {
        if (index <= 0) return false;

        synchronized (lock) {
            @Nullable Element element = get(index).orElse(null);
            if (element == null && getKey(index) == null) return false;

            this.rows.remove(getKey(index));
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
    public @Unmodifiable @NotNull Collection<@NotNull SimpleElement> getAll() {
        return Collections.unmodifiableCollection((rows.values()));
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

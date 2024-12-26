package codes.shawlas.impl.table;

import codes.shawlas.exception.column.DuplicatedColumnException;
import codes.shawlas.exception.column.InvalidColumnException;
import codes.shawlas.exception.column.DuplicatedKeyValueException;
import codes.shawlas.exception.column.MissingKeyException;
import codes.shawlas.exception.table.TableStateException;
import codes.shawlas.table.Column;
import codes.shawlas.table.Element;
import codes.shawlas.table.Table;
import codes.shawlas.table.TableData;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class ElementsImpl implements Table.Elements, Iterable<@NotNull ElementImpl> {

    private final @NotNull Map<@NotNull AutoIncrement, @NotNull ElementImpl> rows = new LinkedHashMap<>();
    private final @NotNull Object lock = new Object();
    private final @NotNull TableImpl table;

    ElementsImpl(@NotNull TableImpl table) {
        this.table = table;
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    public @NotNull ElementImpl create(@NotNull TableData<?> @NotNull ... data) throws MissingKeyException, DuplicatedKeyValueException, TableStateException, InvalidColumnException, DuplicatedColumnException {
        if (table.getColumns().toCollection().isEmpty()) {
            throw new TableStateException("No columns");
        }

        @NotNull Set<@NotNull TableData<?>> dataSet = Arrays.stream(data).collect(Collectors.toSet());
        if (dataSet.size() < data.length) {
            throw new DuplicatedColumnException("Cannot accept duplicated columns");
        }

        @NotNull Set<@NotNull Column<?>> keys = table.getColumns().getKeys();
        @NotNull Set<@NotNull Column<?>> keysData = dataSet.stream().map(TableData::getColumn).filter(Column::isKey).collect(Collectors.toSet());
        @NotNull Set<@NotNull ElementImpl> elements = toCollection();

        if (keys.size() > keysData.size()) {
            throw new MissingKeyException("Some key is missing: " + keys);
        } else if (keys.size() < keysData.size()) {
            throw new InvalidColumnException("The key has exceeded the number of existing keys");
        } else synchronized (lock) {
            final @NotNull UUID id = generate();
            final @NotNull ElementImpl element; element = new ElementImpl(id, table);

            for (@NotNull TableData<?> value : dataSet) {
                if (!table.getColumns().contains(value.getColumn())) {
                    throw new InvalidColumnException("Column does not belong here");
                } else if (value.getColumn().isKey() && elements.stream().anyMatch(e -> e.keysContainsValue(value.getValue()))) {
                    throw new DuplicatedKeyValueException("The key value is already in use: " + value);
                } else {
                    element.values.replace(value.getColumn(), value.getValue());
                }
            }

            rows.put(new AutoIncrement(AutoIncrement.deIn.incrementAndGet()), element);

            return element;
        }
    }

    private @NotNull UUID generate() {
        @NotNull UUID uuid = UUID.randomUUID();

        while (getById(uuid.toString()).isPresent()) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    public boolean delete(long index) {
        @Nullable AutoIncrement autoI = getKey(index).orElse(null);

        if (!contains(index) || autoI == null) return false;

        synchronized (lock) {
            rows.remove(autoI);

            @NotNull Set<@NotNull AutoIncrement> needDecrements = rows.keySet().stream().filter(autoInc -> autoInc.isNeedDecrement(index)).collect(Collectors.toSet());
            for (@NotNull AutoIncrement autoInc : needDecrements) {
                autoInc.decrement();
            }

            AutoIncrement.deIn.decrementAndGet();

            return true;
        }
    }

    @Override
    public boolean deleteById(@NotNull String id) {
        @Nullable Element element = getById(id).orElse(null);
        @Nullable AutoIncrement increment = rows.entrySet().stream().filter(entry -> entry.getValue().equals(element)).findFirst().map(Map.Entry::getKey).orElse(null);

        if (element == null || increment == null || !rows.containsKey(increment)) return false;

        synchronized (lock) {
            rows.remove(increment);

            @NotNull Set<@NotNull AutoIncrement> needDecrements = rows.keySet().stream().filter(inc -> inc.isNeedDecrement(increment.getIndex())).collect(Collectors.toSet());
            for (@NotNull AutoIncrement autoInc : needDecrements) {
                autoInc.decrement();
            }

            AutoIncrement.deIn.decrementAndGet();

            return true;
        }
    }

    @Override
    public @NotNull Optional<@NotNull Element> get(long index) {
        return Optional.ofNullable(rows.get(getKey(index).orElse(null)));
    }

    @Override
    public @NotNull Optional<@NotNull ElementImpl> getById(@NotNull String id) {
        return toCollection().stream().filter(element -> element.getId().equals(id)).findFirst();
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull ElementImpl> toCollection() {
        return Collections.unmodifiableSet(new HashSet<>(rows.values()));
    }

    @Override
    public @NotNull Iterator<@NotNull ElementImpl> iterator() {
        return toCollection().iterator();
    }

    public boolean contains(long index) {
        return rows.containsKey(getKey(index).orElse(null));
    }

    private @NotNull Optional<@NotNull AutoIncrement> getKey(long index) {
        return rows.keySet().stream().filter(inc -> inc.getIndex() == index).findFirst();
    }

    // Native

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Table.Elements) {
            final @NotNull Table.Elements elements = (Table.Elements) o;
            return Objects.equals(this.toCollection(), elements.toCollection());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toCollection());
    }

    // Classes
    private static final class AutoIncrement {

        private static final @NotNull AtomicLong deIn = new AtomicLong(0);

        // Objects

        private final @NotNull AtomicLong aLong;

        public AutoIncrement(@Range(from = 1, to = Long.MAX_VALUE) long index) {
            this.aLong = new AtomicLong(index);
        }

        public boolean isNeedDecrement(long index) {
            return this.getIndex() > index;
        }

        @Range(from = 1, to = Long.MAX_VALUE)
        public long getIndex() {
            return aLong.get();
        }

        public void decrement() {
            aLong.decrementAndGet();
        }

        @Override
        public @NotNull String toString() {
            return String.valueOf(getIndex());
        }

        // Map

        @Override
        public int hashCode() {
            return Objects.hashCode(getIndex());
        }
    }
}

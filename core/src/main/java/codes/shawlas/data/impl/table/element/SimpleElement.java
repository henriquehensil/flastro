package codes.shawlas.data.impl.table.element;

import codes.shawlas.data.exception.column.ColumnTypeException;
import codes.shawlas.data.exception.column.InvalidColumnException;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Element;
import codes.shawlas.data.table.EntryData;
import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimpleElement implements Element {

    private final @NotNull Table table;
    private @Range(from = 1, to = Long.MAX_VALUE) int index;

    private final @NotNull ElementValues values = new ElementValues();
    private final @NotNull ReentrantLock lock;

    SimpleElement(@NotNull Table table, int index) {
        if (!table.getClass().getName().equalsIgnoreCase("codes.shawlas.data.impl.table.SimpleTable")) {
            throw new RuntimeException("This class don't accept another implementation table class: " + table.getClass());
        } else if (index <= 0) {
            throw new RuntimeException("Invalid index");
        } else if (index > table.getElements().getAll().size()) {
            throw new RuntimeException("the new Element cannot be equals or less that total elements in the table");
        }

        @Nullable ReentrantLock lock = null;

        for (@NotNull Method method : table.getClass().getMethods()) {
            if (Modifier.isPrivate(method.getModifiers()) && method.getName().equals("getLock")) {
                lock = (ReentrantLock) method.getDefaultValue();
            }
        }

        if (lock == null) throw new RuntimeException("Cannot find the table global lock");

        this.table = table;
        this.index = index;
        this.lock = lock;
    }

    @NotNull Map<@NotNull Column<?>, @Nullable Object> getValues() {
        return values;
    }

    @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getKeyData() {
        return getData().stream().filter(data -> data.getColumn().isKey()).collect(Collectors.toSet());
    }

    boolean keysContainsValue(@Nullable Object value) {
        return getKeyData().stream().anyMatch(data -> Objects.equals(data.getValue(), value));
    }

    void decrement() {
        index--;
    }

    // Implementations

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) int getIndex() {
        return index;
    }

    @Override
    public <E> @Nullable E getValue(@NotNull Column<E> column) throws InvalidColumnException {
        lock.lock();
        try {
            if (!values.containsKey(column)) {
                throw new InvalidColumnException(column);
            } else {
                //noinspection unchecked
                return (E) values.get(column);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @throws InvalidColumnException if {@code column} not exists
     * @throws ColumnTypeException if {@code column} is key or {@code value} is null but the column is not nullable
     * */
    @Override
    public <E> void setValue(@NotNull Column<E> column, @Nullable E value) throws InvalidColumnException, ColumnTypeException {
        lock.lock();
        try {
            if (!values.containsKey(column)) {
                throw new InvalidColumnException(column);
            } else try {
                values.replace(column, value);
            } catch (IllegalArgumentException e) {
                throw new ColumnTypeException(e.getMessage());
            }
        } finally {
            lock.unlock();
        }
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

    // Classes

    private final class ElementValues extends TreeMap<@NotNull Column<?>, @Nullable Object> {

        private ElementValues() {
            super(Comparator.naturalOrder());
        }

        @Override
        public @Nullable Column<?> put(@NotNull Column<?> column, @Nullable Object value) {
            validate(column, value);

            lock.lock();
            try {
                return (Column<?>) super.put(column, value);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @Nullable Column<?> replace(@NotNull Column<?> column, @Nullable Object value) {
            validate(column, value);
            lock.lock();
            try {
                return (Column<?>) super.replace(column, value);
            } finally {
                lock.unlock();
            }
        }

        private void validate(@NotNull Column<?> column, @Nullable Object value) throws IllegalArgumentException, ClassCastException {
            if ((!column.isNullable() || column.isKey()) && value == null) {
                throw new IllegalArgumentException("This column is " + (column.isKey() ? "key " : "not nullable ") + "and cannot represent a null value");
            } else if (value != null && !Objects.equals(value.getClass(), column.getDataType().getType())) {
                throw new ClassCastException("The class type of the value '" + value + "' is different from the column");
            }
        }

        @Override
        public int size() {
            lock.lock();
            try {
                return super.size();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean containsKey(Object key) {
            lock.lock();
            try {
                return super.containsKey(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean containsValue(Object value) {
            lock.lock();
            try {
                return super.containsValue(value);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @Nullable Column<?> get(Object key) {
            lock.lock();
            try {
                return (Column<?>) super.get(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Comparator<? super @NotNull Column<?>> comparator() {
            lock.lock();
            try {
                return super.comparator();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> firstKey() {
            lock.lock();
            try {
                return super.firstKey();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> lastKey() {
            lock.lock();
            try {
                return super.lastKey();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void putAll(@NotNull Map<? extends @NotNull Column<?>, ?> values) {
            lock.lock();
            try {
                for(@NotNull Map.Entry<? extends @NotNull Column<?>, ?> entry : values.entrySet()) {
                    this.put(entry.getKey(), entry.getValue());
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @Nullable Object remove(Object key) {
            lock.lock();
            try {
                return super.remove(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Object clone() {
            return super.clone();
        }

        @Override
        public @NotNull Set<@NotNull Column<?>> keySet() {
            lock.lock();
            try {
                return super.keySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> firstEntry() {
            lock.lock();
            try {
                return super.firstEntry();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> lastEntry() {
            lock.lock();
            try {
                return super.lastEntry();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> pollFirstEntry() {
            lock.lock();
            try {
                return super.pollFirstEntry();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> pollLastEntry() {
            lock.lock();
            try {
                return super.pollLastEntry();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> lowerEntry(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.lowerEntry(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> lowerKey(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.lowerKey(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> floorEntry(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.floorEntry(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> floorKey(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.floorKey(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> ceilingEntry(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.ceilingEntry(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> ceilingKey(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.ceilingKey(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> higherEntry(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.higherEntry(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Column<?> higherKey(@NotNull Column<?> key) {
            lock.lock();
            try {
                return super.higherKey(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull NavigableSet<@NotNull Column<?>> navigableKeySet() {
            lock.lock();
            try {
                return super.navigableKeySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull NavigableSet<@NotNull Column<?>> descendingKeySet() {
            lock.lock();
            try {
                return super.descendingKeySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Collection<@Nullable Object> values() {
            lock.lock();
            try {
                return super.values();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Set<Map.Entry<@NotNull Column<?>, @Nullable Object>> entrySet() {
            lock.lock();
            try {
                return super.entrySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull NavigableMap<@NotNull Column<?>, @Nullable Object> descendingMap() {
            lock.lock();
            try {
                return super.descendingMap();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull NavigableMap<@NotNull Column<?>, @Nullable Object> subMap(@NotNull Column<?> fromKey, boolean fromInclusive, @NotNull Column<?> toKey, boolean toInclusive) {
            lock.lock();
            try {
                return super.subMap(fromKey, fromInclusive, toKey, toInclusive);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull NavigableMap<@NotNull Column<?>, @Nullable Object> headMap(@NotNull Column<?> toKey, boolean inclusive) {
            lock.lock();
            try {
                return super.headMap(toKey, inclusive);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public NavigableMap<@NotNull Column<?>, @Nullable Object> tailMap(@NotNull Column<?> fromKey, boolean inclusive) {
            lock.lock();
            try {
                return super.tailMap(fromKey, inclusive);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull SortedMap<@NotNull Column<?>, @Nullable Object> subMap(@NotNull Column<?> fromKey, @NotNull Column<?> toKey) {
            lock.lock();
            try {
                return super.subMap(fromKey, toKey);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull SortedMap<@NotNull Column<?>, @Nullable Object> headMap(@NotNull Column<?> toKey) {
            lock.lock();
            try {
                return super.headMap(toKey);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull SortedMap<@NotNull Column<?>, @Nullable Object> tailMap(@NotNull Column<?> fromKey) {
            lock.lock();
            try {
                return super.tailMap(fromKey);
            } finally {
                lock.unlock();
            }
        }

        // unsupported

        @Override
        public boolean replace(@NotNull Column<?> key, @Nullable Object oldValue, @Nullable Object newValue) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public void forEach(BiConsumer<? super @NotNull Column<?>, ? super @Nullable Object> action) {
            super.forEach(action);
        }

        @Override
        public void replaceAll(BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> function) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public @Nullable Object putIfAbsent(@NotNull Column<?> key, @Nullable Object value) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public @Nullable Object computeIfAbsent(@NotNull Column<?> key, @NotNull Function<? super @NotNull Column<?>, ?> mappingFunction) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public @Nullable Object computeIfPresent(@NotNull Column<?> key, @NotNull BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public @Nullable Object compute(@NotNull Column<?> key, @NotNull BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public @Nullable Object merge(@NotNull Column<?> key, @Nullable Object value, @NotNull BiFunction<? super @Nullable Object, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Cannot use this Operation");
        }
    }
}

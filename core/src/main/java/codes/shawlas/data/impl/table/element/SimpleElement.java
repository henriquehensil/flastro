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

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimpleElement implements Element {

    private @Range(from = 1, to = Long.MAX_VALUE) int index;
    private final @NotNull ElementValues values = new ElementValues();
    private final @NotNull Table table;
    private final @NotNull ReentrantLock lock;

    SimpleElement(@NotNull SimpleElements elements, int index) {
        if (index <= 0) throw new IllegalArgumentException("Invalid index");
        this.table = elements.getTable();
        this.index = index;
        this.lock = elements.getLock();

        for (@NotNull Column<?> c : elements.getTable().getColumns().getAll()) {
            values.put(c, c.getDefault());
        }
    }

    @NotNull Map<@NotNull Column<?>, @Nullable Object> getValues() {
        return values;
    }

    @Unmodifiable @NotNull Set<@NotNull EntryData<?>> getKeyData() {
        lock.lock();
        try {
            ((SimpleElements) table.getElements()).upgrade(this);
            return getData().stream().filter(data -> data.getColumn().isKey()).collect(Collectors.toSet());
        } finally {
            lock.unlock();
        }
    }

    boolean keysContainsValue(@Nullable Object value) {
        return getKeyData().stream().anyMatch(data -> Objects.equals(data.getValue(), value));
    }

    void decrement() {
        lock.lock();
        try {
            index--;
        } finally {
            lock.unlock();
        }
    }

    // Implementations

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) int getIndex() {
        lock.lock();
        try {
            return index;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <E> @Nullable E getValue(@NotNull Column<E> column) throws InvalidColumnException {
        lock.lock();
        try {
            ((SimpleElements) table.getElements()).upgrade(this);

            if (!values.containsKey(column)) {
                throw new InvalidColumnException(column);
            }

            //noinspection unchecked
            return (E) values.get(column);

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
            ((SimpleElements) table.getElements()).upgrade(this);

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
        lock.lock();
        try {
            ((SimpleElements) table.getElements()).upgrade(this);

            @NotNull Set<@NotNull EntryData<?>> data = new HashSet<>();

            for (@NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> e : values.entrySet()) {
                // noinspection unchecked
                data.add(new EntryData<>((Column<Object>) e.getKey(), e.getValue()));
            }

            return Collections.unmodifiableSet(data);

        } finally {
            lock.unlock();
        }

    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        lock.lock();
        try {
            ((SimpleElements) table.getElements()).upgrade(this);

            for (@NotNull Map.Entry<@NotNull Column<?>, @Nullable Object> e : values.entrySet()) {
                if (Objects.equals(e.getValue(), value)) {
                    return true;
                }
            }
            return false;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull SimpleElement that = (SimpleElement) o;
        ((SimpleElements) table.getElements()).upgrade(this);
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        ((SimpleElements) table.getElements()).upgrade(this);
        return Objects.hashCode(values);
    }

    // Classes

    private final class ElementValues implements Map<@NotNull Column<?>, @Nullable Object> {

        private final @NotNull Map<@NotNull Column<?>, @Nullable Object> values;

        private ElementValues() {
            this.values = new TreeMap<>(Comparator.naturalOrder());
        }

        @Override
        public @Nullable Object put(@NotNull Column<?> column, @Nullable Object value) {
            validate(column, value);

            lock.lock();
            try {
                return this.values.put(column, value);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @Nullable Object replace(@NotNull Column<?> column, @Nullable Object value) {
            validate(column, value);

            lock.lock();
            try {
                return this.values.replace(column, value);
            } finally {
                lock.unlock();
            }
        }

        private void validate(@NotNull Column<?> column, @Nullable Object value) throws IllegalArgumentException, ClassCastException {
            if ((!column.isNullable() && !column.isKey()) && value == null) {
                throw new IllegalArgumentException("This column is not nullable and cannot represent a null value: " + column);
            } else if (value != null && !Objects.equals(value.getClass(), column.getDataType().getType())) {
                throw new ClassCastException("The class type of the value '" + value + "' is different from the column");
            }
        }

        @Override
        public @Nullable Object remove(@NotNull Object o) {
            if (!isColumn(o)) throw new ClassCastException("the key is of an inappropriate type for this map");

            lock.lock();
            try {
                return values.remove(o);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int size() {
            lock.lock();
            try {
                return values.size();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void putAll(@NotNull Map<? extends @NotNull Column<?>, ?> values) {
            lock.lock();
            try {
                for (@NotNull Entry<? extends @NotNull Column<?>, ?> e : values.entrySet()) {
                    this.put(e.getKey(), e.getValue());
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Set<@NotNull Column<?>> keySet() {
            lock.lock();
            try {
                return this.values.keySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Collection<@Nullable Object> values() {
            lock.lock();
            try {
                return this.values.values();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @NotNull Set<@NotNull Entry<@NotNull Column<?>, @Nullable Object>> entrySet() {
            lock.lock();
            try {
                return this.values.entrySet();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean isEmpty() {
            lock.lock();
            try {
                return this.values.isEmpty();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean containsKey(@NotNull Object o) {
            if (!isColumn(o)) return false;

            lock.lock();
            try {
                return this.values.containsKey(o);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean containsValue(@NotNull Object o) {
            lock.lock();
            try {
                return this.values.containsValue(o);
            } finally {
                lock.unlock();
            }
        }

        private boolean isColumn(@NotNull Object o) {
            return o instanceof Column<?>;
        }

        @Override
        public @Nullable Object get(@NotNull Object column) {
            if (!isColumn(column)) return null;

            lock.lock();
            try {
                return this.values.get(column);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public @Nullable Object getOrDefault(@NotNull Object column, @Nullable Object defaultValue) {
            if (!isColumn(column)) return defaultValue;

            lock.lock();
            try {
                return this.values.getOrDefault(column, defaultValue);
            } finally {
                lock.unlock();
            }
        }

        // Unsupported

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public void forEach(BiConsumer<? super @NotNull Column<?>, ? super @Nullable Object> action) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public void replaceAll(BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> function) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public @Nullable Object putIfAbsent(@NotNull Column<?> key, @Nullable Object value) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public boolean replace(@NotNull Column<?> key, @Nullable Object oldValue, @Nullable Object newValue) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public @Nullable Object computeIfAbsent(@NotNull Column<?> key, @NotNull Function<? super @NotNull Column<?>, ?> mappingFunction) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public @Nullable Object computeIfPresent(@NotNull Column<?> key, @NotNull BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public @Nullable Object compute(@NotNull Column<?> key, @NotNull BiFunction<? super @NotNull Column<?>, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }

        @Override
        public @Nullable Object merge(@NotNull Column<?> key, @Nullable Object value, @NotNull BiFunction<? super @Nullable Object, ? super @Nullable Object, ?> remappingFunction) {
            throw new UnsupportedOperationException("Cannot use this operation");
        }
    }
}
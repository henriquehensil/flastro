package ghostface.dev.impl.table;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.exception.table.TableStateException;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ColumnsImpl implements Columns {

    private final @NotNull Object lock = new Object();
    private final @NotNull Set<@NotNull ColumnImpl<?>>  columnSet = new HashSet<>();
    private final @NotNull TableImpl table;

    ColumnsImpl(@NotNull TableImpl table) {
        this.table = table;
    }

    public @NotNull Set<@NotNull Column<?>> getKeys() {
        return columnSet.stream().filter(ColumnImpl::isKey).collect(Collectors.toSet());
    }

    public @NotNull Set<@NotNull Column<?>> getWithoutKeys() {
        return columnSet.stream().filter(column -> !column.isKey()).collect(Collectors.toSet());
    }

    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws NameAlreadyExistsException {
        if (contains(name)) {
            throw new NameAlreadyExistsException("Column name already exists: " + name);
        } else synchronized (lock) {
            @NotNull ColumnImpl<E> column = new ColumnImpl<>(name, dataType, value, isNullable);
            columnSet.add(column);
            return column;
        }
    }

    @Override
    public @NotNull <E> Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws NameAlreadyExistsException, TableStateException {
        if (contains(name)) {
            throw new NameAlreadyExistsException("Column name already exists: " + name);
        } else if (table.getElements().size() > 0) {
            throw new TableStateException("Cannot possible create new Key Columns now");
        } else synchronized (lock) {
            @NotNull ColumnImpl<E> column = new ColumnImpl<>(name, dataType);
            columnSet.add(column);
            return column;
        }
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> @NotNull Optional<Column<E>> get(@NotNull String name, @NotNull DataType<E> dataType) {
        @Nullable Column<?> column = stream().filter(col -> col.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

        if (column != null) {
            if (column.getDataType().getType().equals(dataType.getType())) {
                return Optional.of((Column<E>) column);
            }
        }

        return Optional.empty();
    }

    public boolean contains(@NotNull String columnName) {
        return toCollection().stream().anyMatch(column -> column.getName().equalsIgnoreCase(columnName));
    }

    @Override
    public @Unmodifiable @NotNull Set<Column<?>> toCollection() {
        return Collections.unmodifiableSet(columnSet);
    }

    public int size() {
        return toCollection().size();
    }

    public @NotNull Stream<Column<?>> stream() {
        return toCollection().stream();
    }

    // Classes

    private final class ColumnImpl<T> implements Column<T> {

        private @NotNull String name;
        private final @NotNull DataType<T> dataType;
        private final @Nullable T value;
        private final boolean isNullable;
        private final boolean isKey;

        ColumnImpl(
                @NotNull String name,
                @NotNull DataType<T> dataType,
                @Nullable T value,
                boolean isNullable
        ) {
            this.name = name;
            this.dataType = dataType;
            this.value = value;
            this.isKey = false;
            this.isNullable = isNullable;
        }

        /**
         * Create a Key Column
         * */
        ColumnImpl(
                @NotNull String name,
                @NotNull DataType<T> dataType
        ) {
            this.name = name;
            this.dataType = dataType;
            this.value = null;
            this.isKey = true;
            this.isNullable = false;
        }

        @Override
        public boolean isKey() {
            return isKey;
        }

        @Override
        public boolean isNullable() {
            return isNullable;
        }

        @Override
        public @NotNull TableImpl getTable() {
            return ColumnsImpl.this.table;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }

        @Override
        public void setName(@NotNull String name) {
            this.name = name;
        }

        @Override
        public @NotNull DataType<T> getDataType() {
            return dataType;
        }

        @Override
        public @UnknownNullability T getDefault() {
            return value;
        }

        // Native

        @Override
        public boolean equals(@Nullable Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull ColumnImpl<?> column = (ColumnImpl<?>) object;
            return isNullable == column.isNullable &&
                    isKey == column.isKey &&
                    Objects.equals(name, column.name) &&
                    Objects.equals(table, column.getTable()) &&
                    Objects.equals(dataType, column.dataType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, table, dataType, isNullable, isKey);
        }
    }
}
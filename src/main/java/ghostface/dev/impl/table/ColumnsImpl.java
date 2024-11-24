package ghostface.dev.impl.table;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExists;
import ghostface.dev.exception.table.TableStateException;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.column.Columns;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

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
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws NameAlreadyExists {
        if (get(name).isPresent()) {
            throw new NameAlreadyExists("Column name already exists: " + name);
        } else {
            @NotNull ColumnImpl<E> column = new ColumnImpl<>(name, dataType, value, isNullable);
            synchronized (lock) {
                columnSet.add(column);
                return column;
            }
        }
    }

    @Override
    public @NotNull <E> Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws NameAlreadyExists, TableStateException {
        if (get(name).isPresent()) {
            throw new NameAlreadyExists("Column name already exists: " + name);
        } else {
            @NotNull ColumnImpl<E> column = new ColumnImpl<>(name, dataType);
            synchronized (lock) {
                columnSet.add(column);
                return column;
            }
        }
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    public @NotNull Optional<Column<?>> get(@NotNull String name) {
        return toCollection().stream().filter(column -> column.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public @Unmodifiable @NotNull Set<Column<?>> toCollection() {
        return Collections.unmodifiableSet(columnSet);
    }

    public int size() {
        return toCollection().size();
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
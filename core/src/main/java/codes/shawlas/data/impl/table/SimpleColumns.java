package codes.shawlas.data.impl.table;

import codes.shawlas.data.DataType;
import codes.shawlas.data.exception.column.ColumnAlreadyExistsException;
import codes.shawlas.data.exception.column.ColumnException;
import codes.shawlas.data.exception.table.NoEmptyTableException;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

final class SimpleColumns implements Table.Columns {

    private final @NotNull Object lock;
    private final @NotNull SimpleTable table;

    private final @NotNull Set<@NotNull Column<?>> columns = new TreeSet<>(Comparator.naturalOrder());

    SimpleColumns(@NotNull SimpleTable table, @NotNull Object lock) {
        this.table = table;
        this.lock = lock;
    }

    @Override
    public @NotNull SimpleTable getTable() {
        return table;
    }

    /**
     * @return The new column Key
     * @throws NoEmptyTableException if already has elements in the table
     * @throws ColumnAlreadyExistsException if the {@code name} is already in use
     * */
    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, NoEmptyTableException {
        synchronized (lock) {
            if (!table.getElements().getAll().isEmpty()) {
                throw new NoEmptyTableException("Cannot create new Key columns because table has elements");
            } else if (get(name).isPresent()) {
                throw new ColumnAlreadyExistsException(get(name).get());
            } else {
                final @NotNull Column<E> column = new SimpleColumn<>(dataType, name);
                columns.add(column);
                getTable().getElements().upgrade(column);
                return column;
            }
        }
    }

    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E defaultValue, boolean isNullable) throws ColumnException {
        synchronized (lock) {
            if (get(name).isPresent()) {
                throw new ColumnAlreadyExistsException(get(name).get());
            } else {
                final @NotNull Column<E> column = new SimpleColumn<>(dataType, name, isNullable, defaultValue);
                columns.add(column);
                getTable().getElements().upgrade(column);
                return column;
            }
        }
    }

    @Override
    public @NotNull Optional<@NotNull Column<?>> get(@NotNull String columnName) {
        synchronized (lock)  {
            return columns.stream().filter(col -> col.getName().equalsIgnoreCase(columnName)).findFirst();
        }
    }

    @Override
    public boolean delete(@NotNull String columnName) {
        if (columns.size() == 1) {
            return false;
        } else synchronized (lock) {
            @Nullable Column<?> column = get(columnName).orElse(null);
            if (column == null) return false;
            columns.remove(column);
            getTable().getElements().upgrade(column);
            return true;
        }
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull Column<?>> getAll() {
        return Collections.unmodifiableSet(columns);
    }

    // Native

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull SimpleColumns that = (SimpleColumns) o;
        return Objects.equals(columns, that.columns);
    }

    // Classes

    private final class SimpleColumn<T> implements Column<T> {

        private @NotNull String name;
        private final @NotNull DataType<T> dataType;
        private final @Nullable T value;
        private final boolean key;
        private final boolean nullable;

        private SimpleColumn(@NotNull DataType<T> dataType, @NotNull String name) {
            this.name = name;
            this.dataType = dataType;
            this.key = true;
            this.nullable = false;
            this.value = null;
        }

        private SimpleColumn(
                @NotNull DataType<T> dataType,
                @NotNull String name,
                boolean isNullable,
                @Nullable T value
        ) {
            this.name = name;
            this.dataType = dataType;
            this.key = false;
            this.nullable = isNullable;
            this.value = value;
        }

        @Override
        public @NotNull SimpleTable getTable() {
            return SimpleColumns.this.table;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }

        @Override
        public @NotNull DataType<T> getDataType() {
            return dataType;
        }

        @Override
        public @Nullable T getDefault() {
            return value;
        }

        @Override
        public boolean isKey() {
            return key;
        }

        @Override
        public boolean isNullable() {
            return nullable;
        }

        @Override
        public void setName(@NotNull String name) {
            this.name = name;
        }

        @Override
        public int compareTo(@NotNull Column<?> col) {
            if (key) return !col.isKey() ? -1 : name.compareTo(col.getName());
            else if (nullable) return !col.isNullable() ? 1 : name.compareTo(col.getName());
            else if (col.isKey()) return 1;
            else if (col.isNullable()) return -1;
            else return name.compareTo(col.getName());
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof Column<?>) {
                @NotNull Column<?> column = (Column<?>) obj;
                return Objects.equals(getTable(), column.getTable()) &&
                        Objects.equals(dataType.getType(), column.getDataType().getType()) &&
                        Objects.equals(name.toLowerCase(), column.getName().toLowerCase()) &&
                        Objects.equals(key, column.isKey()) &&
                        Objects.equals(nullable, column.isNullable());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTable(), getName().toLowerCase(), isKey(), isNullable());
        }
    }
}
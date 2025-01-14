package codes.shawlas.data.impl.table.columns;

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
import java.util.concurrent.locks.ReentrantLock;

final class SimpleColumns implements Table.Columns {

    private final @NotNull Set<@NotNull Column<?>> columns = new TreeSet<>(Comparator.naturalOrder());

    private final @NotNull ReentrantLock lock;
    private final @NotNull Table table;

    private SimpleColumns(@NotNull Table table, @NotNull ReentrantLock lock) {
        if (!table.getClass().getSimpleName().equalsIgnoreCase("SimpleTable")) {
            throw new RuntimeException("This implementation columns only accept an specific table implementation");
        }

        this.lock = lock;
        this.table = table;
    }

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    /**
     * @return The new column Key
     * @throws NoEmptyTableException if already has elements in the table
     * @throws ColumnAlreadyExistsException if the {@code name} is already in use
     * */
    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, NoEmptyTableException {
        lock.lock();
        try {
            if (!table.getElements().getAll().isEmpty()) {
                throw new NoEmptyTableException("Cannot create new Key columns because table has elements");
            } else if (get(name).isPresent()) {
                throw new ColumnAlreadyExistsException(get(name).get());
            } else {
                final @NotNull Column<E> column = new SimpleColumn<>(this, dataType, name);
                columns.add(column);
                return column;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E defaultValue, boolean isNullable) throws ColumnException {
        lock.lock();
        try {
            if (get(name).isPresent()) {
                throw new ColumnAlreadyExistsException(get(name).get());
            } else {
                final @NotNull Column<E> column = new SimpleColumn<>(this, dataType, name, isNullable, defaultValue);
                columns.add(column);
                return column;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NotNull Optional<@NotNull Column<?>> get(@NotNull String columnName) {
        lock.lock();
        try {
            return columns.stream().filter(col -> col.getName().equalsIgnoreCase(columnName)).findFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean delete(@NotNull String columnName) throws NoEmptyTableException {
        lock.lock();
        try {
            @Nullable Column<?> column = get(columnName).orElse(null);
            if (column == null) {
                return false;
            } else if (columns.size() == 1 && !table.getElements().getAll().isEmpty()) {
                throw new NoEmptyTableException("Cannot delete the last column when has elements");
            }

            columns.remove(column);
            return true;
        } finally {
            lock.unlock();
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
        return Objects.equals(columns, that.columns) && Objects.equals(table.getName(), that.table.getName());
    }
}
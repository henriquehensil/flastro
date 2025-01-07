package codes.shawlas.data.impl.table.column;

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

// todo rework locks
final class SimpleColumns implements Table.Columns {

    private final @NotNull Object lock;
    private final @NotNull Set<@NotNull Column<?>> columns = new TreeSet<>(Comparator.naturalOrder());
    private final @NotNull Table table;

    private SimpleColumns(@NotNull Table table, @NotNull Object lock) {
        this.table = table;
        this.lock = lock;
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
        if (table.getElements().getAll().isEmpty()) {
            throw new NoEmptyTableException("Cannot create new Key columns because table has elements");
        } else if (get(name).isPresent()) {
            throw new ColumnAlreadyExistsException(get(name).get());
        } else synchronized (lock) {
            final @NotNull SimpleColumn<E> column = new SimpleColumn<>(dataType, name);
            columns.add(column);
            return column;
        }
    }

    @Override
    public @NotNull <E> Column<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E defaultValue, boolean isNullable) throws ColumnException {
        synchronized (lock) {
            if (get(name).isPresent()) {
                throw new ColumnAlreadyExistsException(get(name).get());
            }

            final @NotNull SimpleColumn<E> column = new SimpleColumn<>(dataType, name, isNullable, defaultValue);
            columns.add(column);
            return column;
        }
    }

    @Override
    public @NotNull Optional<@NotNull Column<?>> get(@NotNull String columnName) {
        return columns.stream().filter(col -> col.getName().equalsIgnoreCase(columnName)).findFirst();
    }

    @Override
    public boolean delete(@NotNull String columnName) {
        synchronized (lock) {
            @Nullable Column<?> column = get(columnName).orElse(null);
            if (column == null) return false;
            columns.remove(column);

            return true;
        }
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull Column<?>> getAll() {
        return Collections.unmodifiableSet(columns);
    }
}
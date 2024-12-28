package codes.shawlas.data.core.impl.table;

import codes.shawlas.data.core.DataType;
import codes.shawlas.data.core.exception.InvalidNameException;
import codes.shawlas.data.core.exception.column.ColumnAlreadyExistsException;
import codes.shawlas.data.core.exception.table.TableStateException;
import codes.shawlas.data.core.table.Column;
import codes.shawlas.data.core.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public final class ColumnsImpl implements Table.Columns, Iterable<@NotNull ColumnImpl<?>> {

    static @NotNull Comparator<@NotNull Column<?>> getComparator() {
        return (col1, col2) -> {
            if (col1.isKey()) {
                return col2.isKey() ? 0 : -1;
            } else if (col1.isNullable()) {
                return col2.isNullable() ? 0 : 1;
            } else {
                return 0;
            }
        };
    }

    // Objects

    private final @NotNull Object lock = new Object();
    private final @NotNull Set<ColumnImpl<?>> columns = new TreeSet<>(getComparator());
    private final @NotNull TableImpl table;

    ColumnsImpl(@NotNull TableImpl table) {
        this.table = table;
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    public @NotNull <E> ColumnImpl<E> create(@NotNull String name, @NotNull DataType<E> dataType, @Nullable E value, boolean isNullable) throws ColumnAlreadyExistsException, InvalidNameException {
        if (get(name).isPresent()) {
            throw new ColumnAlreadyExistsException("Column name already exists: " + name);
        } else synchronized (lock) {
            final @NotNull UUID id = generate();
            final @NotNull ColumnImpl<E> column = new ColumnImpl<>(id, name, table, dataType, isNullable, value);
            columns.add(column);
            return column;
        }
    }

    @Override
    public @NotNull <E> Column<E> createKey(@NotNull String name, @NotNull DataType<E> dataType) throws ColumnAlreadyExistsException, TableStateException, InvalidNameException {
        if (get(name).isPresent()) {
            throw new ColumnAlreadyExistsException("Column name already exists: " + name);
        } else if (!table.getElements().toCollection().isEmpty()) {
            throw new TableStateException("Cannot create new Key columns");
        } else synchronized (lock) {
            final @NotNull UUID id = generate();
            final @NotNull ColumnImpl<E> column = new ColumnImpl<>(id, name, table, dataType);
            columns.add(column);
            return column;
        }
    }

    private @NotNull UUID generate() {
        @NotNull UUID uuid = UUID.randomUUID();

        while (getById(uuid.toString()).isPresent()) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    public boolean contains(@NotNull Column<?> column) {
        return getById(column.getId()).isPresent();
    }

    @Override
    public @NotNull Optional<@NotNull ColumnImpl<?>> get(@NotNull String name) {
        return toCollection().stream().filter(column -> column.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public @NotNull Optional<@NotNull ColumnImpl<?>> getById(@NotNull String id) {
        return toCollection().stream().filter(column -> column.getId().equals(id)).findFirst();
    }

    @Override
    public boolean delete(@NotNull String name) {
        @Nullable ColumnImpl<?> column = get(name).orElse(null);
        if (column == null) {
            return false;
        } else synchronized (lock) {
            columns.remove(column);
            return true;
        }
    }

    @Override
    public boolean deleteById(@NotNull String id) {
        @Nullable ColumnImpl<?> column = getById(id).orElse(null);
        if (column == null) {
            return false;
        } else synchronized (lock) {
            columns.remove(column);
            return true;
        }
    }

    public @NotNull Set<@NotNull Column<?>> getKeys() {
        return toCollection().stream().filter(ColumnImpl::isKey).collect(Collectors.toSet());
    }

    @Override
    public @Unmodifiable @NotNull Set<@NotNull ColumnImpl<?>> toCollection() {
        return Collections.unmodifiableSet(columns);
    }

    @Override
    public @NotNull Iterator<@NotNull ColumnImpl<?>> iterator() {
        return toCollection().iterator();
    }
}

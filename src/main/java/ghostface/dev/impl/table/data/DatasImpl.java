package ghostface.dev.impl.table.data;

import ghostface.dev.exception.table.DuplicatedKeyException;
import ghostface.dev.exception.table.MissingKeyException;
import ghostface.dev.impl.table.ColumnsImpl;
import ghostface.dev.impl.table.TableImpl;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.data.Datas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public final class DatasImpl implements Datas {

    private final @NotNull AtomicInteger increment = new AtomicInteger(0);
    private final @NotNull Map<@NotNull Integer, @NotNull DataImpl> indexes = new LinkedHashMap<>();
    private final @NotNull TableImpl table;

    public DatasImpl(@NotNull TableImpl table) {
        this.table = table;
    }

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    public @NotNull DataImpl create(@NotNull Object @NotNull ... key) throws MissingKeyException, DuplicatedKeyException {
        @NotNull Set<Column<?>> keys = getColumns().getKeys();

        if (!keys.isEmpty()) {
            if (key.length == 0) {
                throw new MissingKeyException("Key values is missing");
            } else for (@NotNull Object obj : key) {
                if (stream().anyMatch(data -> data.hasValue(obj))) {
                    throw new DuplicatedKeyException("This key value is already in use");
                }
            }
        }

        synchronized (this) {
            @NotNull DataImpl data = new DataImpl(getTable());

            if (!keys.isEmpty()) {
                @NotNull Map<@NotNull Column<?>, @NotNull Object> map = new HashMap<>();
                keys.forEach(column -> map.put(column, column.getUtils().generateValue(table)));
                data.getValueMap().putAll(map);
            }

            for (@NotNull Column<?> column : getColumns().toCollection()) {
                if (column.isKey()) continue;
                data.getValueMap().put(column, column.getUtils().getDefaultValue());
            }

            indexes.put(increment.incrementAndGet(), data);
            return data;
        }
    }

    @Override
    public boolean remove(int index) {
        return indexes.remove(index) != null;
    }

    @Override
    public @NotNull Optional<DataImpl> get(int index) {
        return Optional.ofNullable(indexes.get(index));
    }

    @Override
    public @Unmodifiable @NotNull Collection<DataImpl> toCollection() {
        return Collections.unmodifiableCollection(indexes.values());
    }

    public @NotNull ColumnsImpl getColumns() {
        return table.getColumns();
    }

    public @NotNull Stream<DataImpl> stream() {
        return toCollection().stream();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull DatasImpl datas = (DatasImpl) object;
        return Objects.deepEquals(toCollection(), datas.toCollection());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(indexes);
    }
}

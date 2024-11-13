package ghostface.dev.impl.table.data;

import ghostface.dev.exception.table.DuplicatedKeyException;
import ghostface.dev.exception.table.MissingKeyException;
import ghostface.dev.impl.table.ColumnsImpl;
import ghostface.dev.impl.table.TableImpl;
import ghostface.dev.table.Key;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.data.Datas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    public @NotNull DataImpl create(@NotNull Key<?> @NotNull ... keys) throws MissingKeyException, DuplicatedKeyException {
        @NotNull DataImpl data = new DataImpl(getTable());
        @NotNull List<Column<?>> rest = getColumns().toCollection().stream().filter(column -> !column.isKey()).collect(Collectors.toList());

        synchronized (this) {
            for (@NotNull Column<?> column : rest) {
                data.getValueMap().put(column, column.getUtils().getDefaultValue());
            }

            if (keys.length > 0) {
                if (keys.length != getColumns().getKeys().size()) {
                    throw new MissingKeyException("The keys is missing or no provided");
                }

                for (@NotNull Key<?> key : keys) {
                    if (table.getElements().toCollection().stream().anyMatch(element -> element.hasValue(key))) {
                        throw new DuplicatedKeyException("Key already in use");
                    } else {
                        data.getValueMap().put(key.getColumn(), key.getValue());
                    }
                }
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

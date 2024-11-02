package ghostface.dev.mapping;

import ghostface.dev.data.Data;
import ghostface.dev.exception.TableException;
import ghostface.dev.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;

public interface Table<T extends Key<?>> {

    @NotNull String getName();

    @NotNull Data<T> getData(@NotNull T key);

    @NotNull Columns getColumns();

    boolean add(@NotNull Data<T> data) throws TableException;

    boolean remove(@NotNull T key);

    default boolean remove(@NotNull Data<T> data) {
        return remove(data.getKey());
    }

    // collections and map

    @Unmodifiable @NotNull Set<Data<T>> getUnmodifiableData();

    @Unmodifiable @NotNull Set<Key<T>> getUnmodifiableKeys();

    @Unmodifiable @NotNull Map<@NotNull Key<T>, @NotNull Data<T>> getUnmodifiableMap();

}

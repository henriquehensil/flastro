package ghostface.dev.mapping;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Columns extends Iterable<Column<?>> {

    boolean add(@NotNull Column<?> column);

    boolean remove(@NotNull Column<?> column);

    @NotNull Column<?> getColumn(@NotNull String name) throws IllegalArgumentException;

    boolean contains(@NotNull Column<?> column);

    boolean contains(@NotNull String name);

    default boolean isEmpty() {
        return size() == 0;
    }

    int size();

    @Override
    @NotNull Iterator<Column<?>> iterator();
}

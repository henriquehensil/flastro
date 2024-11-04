package ghostface.dev.table.column;

import ghostface.dev.content.Content;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

public final class Columns extends Content.SetProvider<Column<?>> {

    private final @NotNull Table table;

    public Columns(@NotNull Table table) {
        super(new LinkedHashSet<>());
        this.table = table;
    }

    public @NotNull Table getTable() {
        return table;
    }

    public boolean contains(@NotNull String name) {
        return stream().anyMatch(column -> column.getName().equalsIgnoreCase(name));
    }

    public @NotNull Optional<Column<?>> get(@NotNull String name) {
        return stream().filter(column -> column.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public boolean add(@NotNull Column<?> column) {
        if (contains(column.getName()) || contains(column)) return false;

        synchronized (this) {
            return super.add(column);
        }
    }

    @Override
    public boolean remove(@NotNull Column<?> column) {
        if (!contains(column.getName()) || !contains(column)) return false;

        synchronized (this) {
           return super.remove(column);
        }
    }

    @Override
    public boolean contains(@NotNull Column<?> column) {
        return super.contains(column);
    }

    @Override
    public boolean containsAll(@NotNull Collection<Column<?>> collection) {
        return super.containsAll(collection);
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int size() {
        return super.size();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public @Unmodifiable @NotNull Collection<Column<?>> toCollection() {
        return super.toCollection();
    }

    @Override
    public @NotNull Stream<Column<?>> stream() {
        return super.stream();
    }

    @Override
    public @NotNull Iterator<Column<?>> iterator() {
        return super.iterator();
    }
}

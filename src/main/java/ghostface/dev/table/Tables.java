package ghostface.dev.table;

import ghostface.dev.content.Content;
import ghostface.dev.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

public final class Tables extends Content.SetProvider<Table> {

    private final @NotNull Database database;

    public Tables(@NotNull Database database) {
        super(new LinkedHashSet<>());
        this.database = database;
    }

    public @NotNull Database getDatabase() {
        return database;
    }

    public boolean contains(@NotNull String name) {
        return stream().anyMatch(table -> table.getName().equalsIgnoreCase(name));
    }

    public @NotNull Optional<Table> get(@NotNull String name) {
        return stream().filter(table -> table.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public boolean add(@NotNull Table table) {
        if (contains(table.getName())) return false;

        synchronized (this) {
            return super.add(table);
        }
    }

    @Override
    public boolean remove(@NotNull Table table) {
        if (!contains(table.getName())) return false;

        synchronized (this) {
            return super.remove(table);
        }
    }

    @Override
    public boolean contains(@NotNull Table table) {
        return super.contains(table);
    }

    @Override
    public boolean containsAll(@NotNull Collection<Table> collection) {
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
    public @Unmodifiable @NotNull Collection<Table> toCollection() {
        return super.toCollection();
    }

    @Override
    public @NotNull Stream<Table> stream() {
        return super.stream();
    }

    @Override
    public @NotNull Iterator<Table> iterator() {
        return super.iterator();
    }
}

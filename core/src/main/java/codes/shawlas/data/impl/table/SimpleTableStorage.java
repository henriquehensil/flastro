package codes.shawlas.data.impl.table;

import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import codes.shawlas.data.table.Table;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

final class SimpleTableStorage implements TableStorage {

    private final @NotNull Tables tables = new TablesImpl();

    private SimpleTableStorage() {
    }

    @Override
    public @NotNull Tables getTables() {
        return tables;
    }

    // Classes

    private static final class TablesImpl implements Tables {

        private final @NotNull Set<@NotNull Table> tables = new HashSet<>();

        @Override
        public @NotNull Table create(@NotNull String name) throws TableAlreadyExistsException {
            synchronized (this) {
                if (get(name).isPresent()) {
                    throw new TableAlreadyExistsException("The table name '" + name + "' is already in use");
                } else {
                    @NotNull Table table = new SimpleTable(name);
                    tables.add(table);
                    return table;
                }
            }
        }

        @Override
        public boolean remove(@NotNull String name) {
            synchronized (this) {
                @Nullable Table table = get(name).orElse(null);
                if (table == null) return false;

                tables.remove(table);
                return true;
            }
        }

        @Override
        public @NotNull Optional<@NotNull Table> get(@NotNull String name) {
            return tables.stream().filter(table -> table.getName().equalsIgnoreCase(name)).findFirst();
        }

        @Override
        public @NotNull Iterator<@NotNull Table> iterator() {
            return tables.iterator();
        }
    }
}
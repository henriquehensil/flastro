package codes.shawlas.data.core.impl.core;

import codes.shawlas.data.core.exception.table.TableAlreadyExistsException;
import codes.shawlas.data.core.impl.table.TableImpl;
import codes.shawlas.data.core.table.TableStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public final class TableStorageImpl implements TableStorage {

    private final @NotNull DatabaseImpl database;
    private final @NotNull TablesImpl tables = new TablesImpl();

    TableStorageImpl(@NotNull DatabaseImpl database) {
        this.database = database;
    }

    @Override
    public @NotNull DatabaseImpl database() {
        return database;
    }

    @Override
    public @NotNull Tables getTables() {
        return tables;
    }

    // Class
    public static final class TablesImpl implements Tables {

        private final @NotNull Map<@NotNull String, @NotNull TableImpl> tableMap = new HashMap<>();
        private final @NotNull Object lock = new Object();

        @Override
        public @NotNull TableImpl create(@NotNull String name) throws TableAlreadyExistsException {
            if (get(name).isPresent()) {
                throw new TableAlreadyExistsException("Table name already exists: " + name);
            } else synchronized (lock) {
                final @NotNull UUID id = generate();
                final @NotNull TableImpl table = new TableImpl(id.toString());
                tableMap.put(name, table);
                return table;
            }
        }

        private @NotNull UUID generate() {
            @NotNull UUID id = UUID.randomUUID();

            while (getById(id.toString()).isPresent()) {
                id = UUID.randomUUID();
            }

            return id;
        }

        @Override
        public @NotNull Optional<@NotNull TableImpl> get(@NotNull String name) {
            return Optional.ofNullable(tableMap.get(name));
        }

        @Override
        public @NotNull Optional<@NotNull TableImpl> getById(@NotNull String id) {
            return toCollection().stream().filter(table -> table.getId().equals(id)).findFirst();
        }

        @Override
        public boolean delete(@NotNull String name) {
            @Nullable TableImpl table = get(name).orElse(null);
            if (table == null) {
                return false;
            } else synchronized (lock) {
                tableMap.remove(name);
                return true;
            }
        }

        @Override
        public boolean deleteById(@NotNull String id) {
            @Nullable TableImpl table = getById(id).orElse(null);
            if (table == null) {
                return false;
            } else synchronized (lock) {
                @Nullable String name = tableMap.entrySet().stream().filter(e -> e.getValue().equals(table)).map(Map.Entry::getKey).findFirst().orElse(null);
                return tableMap.remove(name) != null;
            }
        }

        @Override
        public @Unmodifiable @NotNull Set<@NotNull TableImpl> toCollection() {
            return Collections.unmodifiableSet(new HashSet<>(tableMap.values()));
        }
    }
}

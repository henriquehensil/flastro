//package ghostface.dev.impl;
//
//import ghostface.dev.exception.NameAlreadyExistsException;
//import ghostface.dev.impl.table.TableImpl;
//import ghostface.dev.table.Table;
//import ghostface.dev.table.TableStorage;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Unmodifiable;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//public final class TableStorageImpl implements TableStorage {
//
//    private final @NotNull TablesImpl tables = new TablesImpl();
//    private final @NotNull DatabaseImpl database;
//
//    TableStorageImpl(@NotNull DatabaseImpl database) {
//        this.database = database;
//    }
//
//    @Override
//    public @NotNull DatabaseImpl database() {
//        return database;
//    }
//
//    @Override
//    public @NotNull TablesImpl getTables() {
//        return tables;
//    }
//
//    // Classes
//
//    public final class TablesImpl implements Tables {
//
//        private final @NotNull Map<@NotNull String, @NotNull Table> tableMap = new ConcurrentHashMap<>();
//
//        private TablesImpl() {
//        }
//
//        @Override
//        public @NotNull TableStorageImpl getStorage() {
//            return TableStorageImpl.this;
//        }
//
//        @Override
//        public @NotNull Table create(@NotNull String name) throws NameAlreadyExistsException {
//            if (tableMap.containsKey(name.toLowerCase())) {
//                throw new NameAlreadyExistsException("name is already in use");
//            }
//
//            @NotNull Table table = new TableImpl(getTableStorage());
//            tableMap.put(name.toLowerCase(), table);
//
//            return table;
//        }
//
//        @Override
//        public @NotNull Optional<@NotNull Table> get(@NotNull String name) {
//            return Optional.ofNullable(tableMap.get(name.toLowerCase()));
//        }
//
//        @Override
//        public boolean delete(@NotNull String name) {
//            return tableMap.remove(name.toLowerCase()) != null;
//        }
//
//        @Override
//        public @Unmodifiable @NotNull Collection<@NotNull Table> toCollection() {
//            return Collections.unmodifiableCollection(tableMap.values());
//        }
//    }
//}
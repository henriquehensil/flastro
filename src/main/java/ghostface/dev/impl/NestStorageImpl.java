//package ghostface.dev.impl;
//
//import ghostface.dev.DataType;
//import ghostface.dev.exception.NameAlreadyExistsException;
//import ghostface.dev.impl.nest.NestImpl;
//import ghostface.dev.nest.Nest;
//import ghostface.dev.nest.NestStorage;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.annotations.Unmodifiable;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//public final class NestStorageImpl implements NestStorage {
//
//    private final @NotNull NestsImpl nests = new NestsImpl();
//    private final @NotNull DatabaseImpl database;
//
//    NestStorageImpl(@NotNull DatabaseImpl database) {
//        this.database = database;
//    }
//
//    @Override
//    public @NotNull DatabaseImpl database() {
//        return database;
//    }
//
//    @Override
//    public @NotNull NestsImpl getNests() {
//        return nests;
//    }
//
//    // Classes
//
//    public final class NestsImpl implements Nests {
//
//        private final @NotNull Map<@NotNull String, @NotNull NestImpl<?>> nestMap = new ConcurrentHashMap<>();
//
//        private NestsImpl() {
//        }
//
//        @Override
//        public @NotNull NestStorageImpl getNestStorage() {
//            return NestStorageImpl.this;
//        }
//
//        @Override
//        public @NotNull <E> NestImpl<E> create(@NotNull String id, @NotNull DataType<E> dataType) throws NameAlreadyExistsException {
//            if (nestMap.containsKey(id.toLowerCase())) {
//                throw new NameAlreadyExistsException("Name is already in use");
//            }
//
//            @NotNull NestImpl<E> nest = new NestImpl<>(getNestStorage(), dataType);
//            nestMap.put(id.toLowerCase(), nest);
//            return nest;
//        }
//
//        @SuppressWarnings("unchecked")
//        public <E> @NotNull Optional<@NotNull NestImpl<E>> get(@NotNull String id, @NotNull DataType<E> dataType) {
//            @Nullable NestImpl<?> nest = nestMap.get(id.toLowerCase());
//
//            boolean types = nest != null && nest.getDataType().getType().equals(dataType.getType());
//            if (!types) return Optional.empty();
//
//            return Optional.of((NestImpl<E>) nest);
//        }
//
//        @Override
//        public @NotNull Optional<@NotNull NestImpl<?>> get(@NotNull String id) {
//            return Optional.ofNullable(nestMap.get(id));
//        }
//
//        @Override
//        public boolean delete(@NotNull String id) {
//            return nestMap.remove(id.toLowerCase()) != null;
//        }
//
//        @Override
//        public @Unmodifiable @NotNull Collection<@NotNull Nest<?>> toCollection() {
//            return Collections.unmodifiableCollection(nestMap.values());
//        }
//    }
//}
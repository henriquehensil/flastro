package ghostface.dev.impl.nest;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.impl.NestStorageImpl;
import ghostface.dev.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NestImpl<T> implements Nest<T> {

    private final @NotNull Map<@NotNull String, @NotNull NestImpl<?>> subs = new ConcurrentHashMap<>();
    private final @NotNull Map<@NotNull String, @NotNull T> values = new ConcurrentHashMap<>();

    private final @NotNull NestStorageImpl storage;
    private final @NotNull DataType<T> dataType;


    public NestImpl(@NotNull NestStorageImpl storage, @NotNull DataType<T> dataType) {
        this.dataType = dataType;
        this.storage = storage;
    }

    @Override
    public @NotNull <E> Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NameAlreadyExistsException {
        if (subs.containsKey(id.toLowerCase())) {
            throw new NameAlreadyExistsException("ID already in use");
        } else {
            @NotNull NestImpl<E> nest = new NestImpl<>(getNestStorage(), dataType);
            subs.put(id.toLowerCase(), nest);
            return nest;
        }
    }

    @Override
    public boolean put(@NotNull String key, T value) {
        if (values.containsKey(key.toLowerCase())) return false;

        values.put(key.toLowerCase(), value);
        return true;
    }

    @Override
    public @NotNull NestStorageImpl getNestStorage() {
        return storage;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <E> Optional<Nest<E>> getSub(@NotNull String id, @NotNull DataType<E> dataType) {
        @Nullable Nest<?> nest = subs.get(id.toLowerCase());

        boolean types = nest != null && nest.getDataType().getType().equals(dataType.getType());
        if (!types) return Optional.empty();

        return Optional.of((Nest<E>) nest);
    }

    @Override
    public @NotNull Optional<T> getValue(@NotNull String key) {
        return Optional.ofNullable(values.get(key.toLowerCase()));
    }

    @Override
    public @NotNull DataType<T> getDataType() {
        return dataType;
    }

    @Override
    public @Unmodifiable @NotNull Collection<T> toCollection() {
        return Collections.unmodifiableCollection(new HashSet<>(values.values()));
    }
}
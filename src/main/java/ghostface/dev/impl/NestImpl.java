package ghostface.dev.impl;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NestImpl<T> implements Nest<T> {

    private final @NotNull Map<@NotNull String, @NotNull NestImpl<?>> subs = new ConcurrentHashMap<>();
    private final @NotNull Map<@NotNull String, @NotNull T> values = new ConcurrentHashMap<>();
    private final @NotNull DataType<T> dataType;

    public NestImpl(@NotNull DataType<T> dataType) {
        this.dataType = dataType;
    }

    @Override
    public @NotNull <E> Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NameAlreadyExistsException {
        if (subs.containsKey(id.toLowerCase())) {
            throw new NameAlreadyExistsException("ID already in use");
        } else {
            @NotNull NestImpl<E> nest = new NestImpl<>(dataType);
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
    @SuppressWarnings("unchecked")
    public @NotNull <E> Optional<Nest<E>> getSub(@NotNull String id, @NotNull DataType<E> dataType) {
        @NotNull Optional<Nest<E>> nest = Optional.ofNullable((Nest<E>) subs.get(id.toLowerCase()));

        if (!nest.isPresent()) {
            return nest;
        } else if (!nest.get().getDataType().getType().equals(dataType.getType())) {
            return Optional.empty();
        }

        return nest;
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
    public @Unmodifiable @NotNull Set<? extends T> toCollection() {
        return Collections.unmodifiableSet(new HashSet<>(values.values()));
    }
}
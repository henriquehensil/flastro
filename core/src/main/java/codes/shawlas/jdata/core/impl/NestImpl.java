package codes.shawlas.jdata.core.impl;

import codes.shawlas.jdata.core.DataType;
import codes.shawlas.jdata.core.exception.nest.NestAlreadyExistsException;
import codes.shawlas.jdata.core.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public final class NestImpl<T> implements Nest<T> {

    private final @NotNull Object lock = new Object();
    private final @NotNull Map<@NotNull String, @NotNull T> values = new HashMap<>();
    private final @NotNull UUID id;
    private final @NotNull DataType<T> dataType;
    private final @NotNull SubNests children = new SubNestsImpl();
    private final @Nullable Nest<?> father;

    public NestImpl(@NotNull UUID id, @NotNull DataType<T> dataType, @NotNull Nest<?> father) {
        this.id = id;
        this.dataType = dataType;
        this.father = father;
    }

    public NestImpl(@NotNull UUID id, @NotNull DataType<T> dataType) {
        this.id = id;
        this.dataType = dataType;
        this.father = null;
    }

    @Override
    public @NotNull String getId() {
        return id.toString();
    }

    @Override
    public @NotNull Optional<T> getValue(@NotNull String key) {
        return Optional.ofNullable(values.get(key));
    }

    @Override
    public boolean put(@NotNull String key, T value) {
        if (!values.containsKey(key)) {
            return false;
        } else synchronized (lock) {
            values.put(key, value);
            return true;
        }
    }

    @Override
    public @Nullable Nest<?> getFather() {
        return father;
    }

    @Override
    public @NotNull SubNests getSubs() {
        return children;
    }

    @Override
    public @NotNull DataType<T> getDataType() {
        return dataType;
    }

    @Override
    public @Unmodifiable @NotNull Collection<T> getValues() {
        return Collections.unmodifiableCollection(values.values());
    }

    // Native

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Nest<?>) {
            final @NotNull Nest<?> that = (Nest<?>) o;
            return Objects.equals(this.getId(), that.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // Classes
    private final class SubNestsImpl implements SubNests {

        private final @NotNull Map<@NotNull String, @NotNull Nest<?>> subs = new HashMap<>();

        @Override
        public @NotNull <E> Nest<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws NestAlreadyExistsException {
            name = name.toLowerCase();
            if (subs.get(name) != null) {
                throw new NestAlreadyExistsException("Nest id is already in use");
            } else synchronized (lock) {
                @NotNull UUID uuid = generate();
                @NotNull Nest<E> nest = new NestImpl<>(uuid, dataType, NestImpl.this);
                subs.put(name, nest);
                return nest;
            }
        }

        private @NotNull UUID generate() {
            @NotNull UUID uuid = UUID.randomUUID();

            while (subs.containsKey(uuid.toString())) {
                uuid = UUID.randomUUID();
            }

            return uuid;
        }

        @Override
        public @NotNull Optional<@NotNull Nest<?>> get(@NotNull String name) {
            return Optional.ofNullable(subs.get(name));
        }

        @Override
        public @NotNull Optional<@NotNull Nest<?>> getById(@NotNull String id) {
            return subs.values().stream().filter(nest -> nest.getId().equals(id)).findFirst();
        }

        @Override
        public boolean delete(@NotNull String name) {
            if (!subs.containsKey(name)) {
                return false;
            } else synchronized (lock) {
                subs.remove(name);
                return true;
            }
        }

        @Override
        public boolean deleteById(@NotNull String id) {
            @Nullable Map.Entry<@NotNull String, @NotNull Nest<?>> entry = subs.entrySet().stream().filter(e -> e.getValue().getId().equals(id)).findFirst().orElse(null);

            if (entry == null || !subs.containsKey(entry.getKey())) {
                return false;
            } else synchronized (lock) {
                subs.remove(entry.getKey());
                return true;
            }
        }

        @Override
        public @Unmodifiable @NotNull Set<@NotNull Nest<?>> toCollection() {
            return Collections.unmodifiableSet(new HashSet<>(subs.values()));
        }

        @Override
        public boolean exists(@NotNull String id) {
            return getById(id).isPresent();
        }
    }
}

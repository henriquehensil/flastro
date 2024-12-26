package codes.shawlas.impl.core;

import codes.shawlas.DataType;
import codes.shawlas.database.Database;
import codes.shawlas.exception.InvalidNameException;
import codes.shawlas.exception.NameAlreadyExistsException;
import codes.shawlas.exception.nest.NestAlreadyExistsException;
import codes.shawlas.impl.NestImpl;
import codes.shawlas.nest.Nest;
import codes.shawlas.nest.NestStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.regex.Pattern;

public final class NestStorageImpl implements NestStorage {

    private static final @NotNull Pattern namePattern = Pattern.compile("^[a-z0-9-_.]{2,63}$");

    public static @NotNull Pattern getNamePattern() {
        return namePattern;
    }

    private final @NotNull NestsImpl nests = new NestsImpl();
    private final @NotNull Database database;

    NestStorageImpl(@NotNull Database database) {
        this.database = database;
    }

    @Override
    public @NotNull Database database() {
        return database;
    }

    @Override
    public @NotNull NestsImpl getNests() {
        return nests;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull NestStorageImpl that = (NestStorageImpl) o;
        return Objects.equals(nests, that.nests);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nests);
    }

    // Classes
    public static final class NestsImpl implements Nests {

        private final @NotNull Map<@NotNull String, @NotNull NestImpl<?>> subs = new HashMap<>();

        /**
         * @throws InvalidNameException if {@code name} is an invalid syntax (See #getPattern)
         * @throws NameAlreadyExistsException if {@code name} is already in use
         * */
        @Override
        public @NotNull <E> NestImpl<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws NestAlreadyExistsException, InvalidNameException {
            name = name.toLowerCase();
            if (!name.matches(getNamePattern().pattern())) {
                throw new InvalidNameException("Invalid syntax name");
            } else if (subs.get(name) != null) {
                throw new NestAlreadyExistsException("Nest id is already in use");
            } else synchronized (this) {
                @NotNull UUID uuid = generate();
                @NotNull NestImpl<E> nest = new NestImpl<>(uuid, dataType);
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
        public @NotNull Optional<@NotNull NestImpl<?>> get(@NotNull String name) {
            return Optional.ofNullable(subs.get(name));
        }

        @Override
        public @NotNull Optional<@NotNull NestImpl<?>> getById(@NotNull String id) {
            return subs.values().stream().filter(nest -> nest.getId().equals(id)).findFirst();
        }

        @Override
        public boolean delete(@NotNull String name) {
            if (!subs.containsKey(name)) {
                return false;
            } else synchronized (this) {
                subs.remove(name);
                return true;
            }
        }

        @Override
        public boolean deleteById(@NotNull String id) {
            @Nullable Map.Entry<@NotNull String, @NotNull NestImpl<?>> entry = subs.entrySet().stream().filter(e -> e.getValue().getId().equals(id)).findFirst().orElse(null);

            if (entry == null || !subs.containsKey(entry.getKey())) {
                return false;
            } else synchronized (this) {
                subs.remove(entry.getKey());
                return true;
            }
        }

        @Override
        public @Unmodifiable @NotNull Set<@NotNull Nest<?>> toCollection() {
            return Collections.unmodifiableSet(new HashSet<>(subs.values()));
        }

        // Native

        @Override
        public boolean equals(@Nullable Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final @NotNull NestsImpl nests = (NestsImpl) o;
            return Objects.equals(subs, nests.subs);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(subs);
        }
    }
}

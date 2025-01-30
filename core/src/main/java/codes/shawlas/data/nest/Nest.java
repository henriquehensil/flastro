package codes.shawlas.data.nest;

import codes.shawlas.data.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public interface Nest<T> {

    @NotNull String name();

    @NotNull String getId();

    @NotNull DataType<T> getDataType();

    @NotNull Optional<@NotNull Nest<?>> getFather();

    @NotNull NestStorage.Nests getChildren();

    @NotNull Content<T> getContent();

    // Classes

    interface Content<T> extends Map<@NotNull String, @Nullable T>, Iterable<@NotNull T> {

        @Override
        @Nullable T put(@NotNull String key, @Nullable T value);

        @Nullable T get(@NotNull String key);

        @Override
        default @Nullable T get(@NotNull Object key) {
            return key instanceof String ? get((String) key) : null;
        }

        @Override
        @Unmodifiable @NotNull Set<@NotNull String> keySet();

        @Override
        @Unmodifiable @NotNull Collection<@Nullable T> values();

        @Override
        default @NotNull Iterator<@NotNull T> iterator() {
            return values().iterator();
        }

        @Override
        default int size() {
            return keySet().size();
        }

        @Override
        default boolean isEmpty() {
            return keySet().isEmpty();
        }

        default boolean containsKey(@NotNull String key) {
            return get(key) != null;
        }

        @Override
        default boolean containsKey(@NotNull Object key) {
            return key instanceof String && containsKey((String) key);
        }

        @Override
        boolean containsValue(@Nullable Object value);

        @Nullable T remove(@NotNull String key);

        @Override
        default @Nullable T remove(@NotNull Object key) {
            if (!(key instanceof String)) throw new ClassCastException("Key is not a String");
            else return remove((String) key);
        }

        @Override
        void putAll(@NotNull Map<? extends @NotNull String, ? extends @Nullable T> map);

        @Override
        void clear();

        @Override
        @NotNull Set<@NotNull Entry<@NotNull String, @Nullable T>> entrySet();

    }
}
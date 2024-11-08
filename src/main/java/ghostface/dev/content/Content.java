package ghostface.dev.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public interface Content<T> extends Iterable<T> {

    boolean add(@NotNull T object);

    boolean remove(@NotNull T object);

    boolean contains(@NotNull T object);

    boolean containsAll(@NotNull Collection<T> collection);

    @Range(from = 0, to = Integer.MAX_VALUE)
    int size();

    void clear();

    default boolean isEmpty() {
        return size() == 0;
    }

    @Unmodifiable @NotNull Collection<T> toCollection();

    @NotNull Stream<T> stream();

    // providers

    class SetProvider<T> implements Content<T> {

        private final @NotNull Set<T> collection;

        public SetProvider(@NotNull Set<T> collection) {
            this.collection = collection;
        }

        @Override
        public boolean add(@NotNull T object) {
            return collection.add(object);
        }

        @Override
        public boolean remove(@NotNull T object) {
            return collection.remove(object);
        }

        @Override
        public boolean contains(@NotNull T object) {
            return collection.contains(object);
        }

        @Override
        public boolean containsAll(@NotNull Collection<T> collection) {
            return this.collection.containsAll(collection);
        }

        @Override
        public @Range(from = 0, to = Integer.MAX_VALUE) int size() {
            return collection.size();
        }

        @Override
        public void clear() {
            collection.clear();
        }

        @Override
        public @Unmodifiable @NotNull Collection<T> toCollection() {
            return Collections.unmodifiableCollection(collection);
        }

        @Override
        public @NotNull Stream<T> stream() {
            return collection.stream();
        }

        @Override
        public @NotNull Iterator<T> iterator() {
            return collection.iterator();
        }
    }
}

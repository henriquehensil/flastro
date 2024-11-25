package ghostface.dev.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface KeyContent<K, V> extends UnmodifiableContent<V> {

    @NotNull Optional<? extends V> get(@NotNull K id);

    boolean put(@NotNull K id, @NotNull V value);

    boolean delete(@NotNull K id);

    @Override
    @Unmodifiable @NotNull Collection<? extends V> toCollection();
}

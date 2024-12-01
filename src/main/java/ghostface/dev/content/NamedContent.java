package ghostface.dev.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NamedContent<V> extends UnmodifiableContent<V> {

    @NotNull Optional<? extends V> get(@NotNull String name);

    boolean delete(@NotNull String name);

    @Override
    @Unmodifiable @NotNull Collection<V> toCollection();
}

package codes.shawlas.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NamedContent<V> {

    @NotNull Optional<? extends @NotNull V> get(@NotNull String name);

    boolean delete(@NotNull String name);

    @Unmodifiable @NotNull Collection<? extends @NotNull V> toCollection();
}

package codes.shawlas.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NamedContent<V> {

    @NotNull Optional<? extends @NotNull V> get(@NotNull String name);

    @NotNull Optional<? extends @NotNull V> getById(@NotNull String id);

    boolean delete(@NotNull String name);

    boolean deleteById(@NotNull String id);

    @Unmodifiable @NotNull Collection<? extends @NotNull V> toCollection();
}

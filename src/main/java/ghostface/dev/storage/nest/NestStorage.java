package ghostface.dev.storage.nest;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface NestStorage {

    @NotNull Optional<Nest<?>> get(@NotNull String id);

    boolean add(@NotNull Nest<?> cube);

    boolean remove(@NotNull Nest<?> cube);

    boolean remove(@NotNull String id);

}

package ghostface.dev.storage;

import ghostface.dev.content.KeyContent;
import ghostface.dev.database.Database;
import ghostface.dev.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage extends KeyContent<String, Nest<?>> {

    @NotNull Database database();

    // Implementations

    @Override
    @NotNull Optional<? extends Nest<?>> get(@NotNull String id);

    @Override
    boolean put(@NotNull String id, @NotNull Nest<?> nest);

    @Override
    boolean delete(@NotNull String id);

    @Override
    @Unmodifiable @NotNull Collection<? extends Nest<?>> toCollection();
}

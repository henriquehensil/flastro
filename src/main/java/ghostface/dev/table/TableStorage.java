package ghostface.dev.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface TableStorage {

    @NotNull Table create(@NotNull String name);

    @NotNull Optional<? extends Table> get(@NotNull String name);

    void put(@NotNull String key, @NotNull Table table);

    boolean remove(@NotNull String key);

    @Unmodifiable @NotNull Collection<? extends Table> toCollection();

}

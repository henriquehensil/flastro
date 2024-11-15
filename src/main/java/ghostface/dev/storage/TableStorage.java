package ghostface.dev.storage;

import ghostface.dev.content.KeyContent;
import ghostface.dev.database.Database;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface TableStorage extends KeyContent<String, Table> {

    @NotNull Database database();

    // Implementations

    @Override
    @NotNull Optional<@NotNull Table> get(@NotNull String id);

    @Override
    boolean put(@NotNull String id, @NotNull Table table);

    @Override
    boolean delete(@NotNull String id);

    @Override
    @Unmodifiable @NotNull Collection<? extends Table> toCollection();
}

package ghostface.dev.storage;

import ghostface.dev.content.NamedContent;
import ghostface.dev.database.Database;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;


public interface TableStorage extends NamedContent<@NotNull Table> {

    @NotNull Database database();

    /**
     * @throws NameAlreadyExistsException if {@code name} is already in use
     * */
    @NotNull Table create(@NotNull String name) throws NameAlreadyExistsException;

    // Implementations

    @Override
    @NotNull Optional<? extends @NotNull Table> get(@NotNull String id);

    @Override
    boolean delete(@NotNull String name);

    @Override
    @Unmodifiable @NotNull Collection<? extends @NotNull Table> toCollection();
}
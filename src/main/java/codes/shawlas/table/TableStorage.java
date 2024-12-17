package codes.shawlas.table;

import codes.shawlas.content.NamedContent;
import codes.shawlas.database.Database;
import codes.shawlas.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface TableStorage {

    @NotNull Database database();

    @NotNull Tables getTables();

    // Classes

    interface Tables extends NamedContent<@NotNull Table> {

        @NotNull TableStorage getStorage();

        /**
         * @throws TableAlreadyExistsException if {@code name} is already in use
         * */
        @NotNull Table create(@NotNull String name) throws TableAlreadyExistsException;

        // Implementations

        @Override
        @NotNull Optional<? extends @NotNull Table> get(@NotNull String id);

        @Override
        boolean delete(@NotNull String name);

        @Override
        @Unmodifiable @NotNull Collection<@NotNull Table> toCollection();
    }
}
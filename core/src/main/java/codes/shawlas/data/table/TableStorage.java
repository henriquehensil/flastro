package codes.shawlas.data.table;

import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface TableStorage {

    @NotNull Tables getTables();

    // Classes

    interface Tables extends Iterable<@NotNull Table> {

        /**
         * @throws TableAlreadyExistsException if table {@code name} is already in use
         * */
        @NotNull Table create(@NotNull String name) throws TableAlreadyExistsException;

        boolean remove(@NotNull String name);

        @NotNull Optional<@NotNull Table> get(@NotNull String name);

    }
}

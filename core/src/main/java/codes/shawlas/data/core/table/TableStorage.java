package codes.shawlas.data.core.table;

import codes.shawlas.data.core.content.NamedContent;
import codes.shawlas.data.core.database.Database;
import codes.shawlas.data.core.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;

public interface TableStorage {

    @NotNull Database database();

    @NotNull Tables getTables();

    // Classes

    interface Tables extends NamedContent<@NotNull Table> {

        /**
         * @throws TableAlreadyExistsException if {@code name} is already in use
         * */
        @NotNull Table create(@NotNull String name) throws TableAlreadyExistsException;
    }
}
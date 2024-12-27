package codes.shawlas.jdata.core.table;

import codes.shawlas.jdata.core.content.NamedContent;
import codes.shawlas.jdata.core.database.Database;
import codes.shawlas.jdata.core.exception.table.TableAlreadyExistsException;
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
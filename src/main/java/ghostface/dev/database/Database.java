package ghostface.dev.database;

import ghostface.dev.mapping.Table;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull Set<Table<?>> getTables();

    boolean start() throws IOException;

    boolean stop() throws IOException;

    boolean add(@NotNull Table<?> table);

    boolean remove(@NotNull Table<?> table);

    boolean contains(@NotNull Table<?> table);
}

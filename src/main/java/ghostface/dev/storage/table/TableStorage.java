package ghostface.dev.storage.table;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public interface TableStorage {

    @NotNull Optional<Table> get(@NotNull String name);

    boolean add(@NotNull Table table);

    boolean remove(@NotNull Table table);

    default boolean contains(@NotNull Table table) {
        return get(table.getName()).isPresent();
    }

}

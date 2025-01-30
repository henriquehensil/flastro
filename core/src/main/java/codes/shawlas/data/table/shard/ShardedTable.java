package codes.shawlas.data.table.shard;

import codes.shawlas.data.connection.Authentication;
import codes.shawlas.data.exception.table.column.InvalidColumnException;
import codes.shawlas.data.exception.table.shard.IllegalDatabaseException;
import codes.shawlas.data.table.standard.Column;
import codes.shawlas.data.table.standard.Element;
import codes.shawlas.data.table.EntryData;
import codes.shawlas.data.table.standard.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiPredicate;

public interface ShardedTable extends Table {

    @NotNull Shards getShards();

    @Override
    @NotNull ShardedElements getElements();

    @Override
    @NotNull ShardedColumns getColumns();

    // Classes

    interface ShardedElements extends Elements {

        @Override
        @NotNull ShardedTable getTable();

        @NotNull Optional<? extends @NotNull Element> get(@NotNull Shard shard, @NotNull String table, int row);

        boolean delete(@NotNull Shard shard, @NotNull String table, int row) ;

    }

    interface ShardedColumns extends Columns {

        @Override
        @NotNull ShardedTable getTable();

        @NotNull Collection<? extends @NotNull Column<?>> getShattered();

    }

    interface Shards {

        @NotNull ShardedTable getTable();

        /**
         * @throws IllegalArgumentException if the priority is zero or negative
         * @throws IllegalDatabaseException if the specific database is not registered and cannot be sharded
         * @throws IOException if an I/O error occurs while trying to connect in another database
         * */
        @NotNull Shard apply(@NotNull Authentication authentication, @NotNull BiPredicate<@NotNull Column<?>, @Nullable Object> predicate, int priority) throws IllegalDatabaseException, IOException;

        /**
         * Get the shard with the highest priority associate to a specific element parameter.
         *
         * @return The highest shard.
         *
         * @throws IllegalStateException if no one shards was defined
         * @throws IllegalArgumentException if the element does not exist
         * */
        @NotNull Shard get(@NotNull Element element);

        @NotNull Optional<? extends @NotNull Shard> get(@NotNull EntryData<?> data);

        @NotNull Optional<? extends @NotNull Shard> get(@NotNull String id);

        @Unmodifiable @NotNull Collection<? extends @NotNull Shard> getAll();

    }
}
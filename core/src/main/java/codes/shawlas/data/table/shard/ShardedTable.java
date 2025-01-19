package codes.shawlas.data.table.shard;

import codes.shawlas.data.connection.Authentication;
import codes.shawlas.data.connection.Database;
import codes.shawlas.data.exception.table.column.InvalidColumnException;
import codes.shawlas.data.exception.table.shard.InvalidTableException;
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

    @NotNull Database getDatabase();

    @NotNull ShardedElements getElements();

    @NotNull Shards getShards();

    // Classes

    interface ShardedElements extends Elements {

        @NotNull ShardedTable getTable();

        @NotNull Optional<? extends @NotNull Element> get(@NotNull Shard shard, int row);

        boolean delete(@NotNull Shard shard, int row) ;

    }

    interface ShardedColumns extends Columns {

        @NotNull ShardedTable getTable();

        @NotNull Collection<? extends @NotNull Column<?>> getShattered();

    }

    interface Shards {

        @NotNull ShardedTable getTable();

        /**
         * @throws IllegalArgumentException if the priority is zero or negative
         * @throws IllegalDatabaseException if the specific database is not registered and cannot be sharded
         * @throws IOException if database is closed
         * */
        @NotNull Shard apply(@NotNull Database database, @NotNull BiPredicate<@NotNull Column<?>, @Nullable Object> predicate, int priority) throws InvalidTableException, IllegalDatabaseException, IllegalArgumentException, IOException;

        /**
         * @throws IOException if an I/O error occurs when
         * */
        @NotNull Database register(@NotNull Authentication authentication) throws IOException;

        @Unmodifiable @NotNull Collection<? extends @NotNull Shard> getAll();

        @Unmodifiable @NotNull Collection<? extends @NotNull Shard> getAll(@NotNull EntryData<?> data) throws InvalidColumnException;

    }
}
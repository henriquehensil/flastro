package codes.shawlas.data.table.shard;

import codes.shawlas.data.connection.Database;
import codes.shawlas.data.table.standard.Column;
import codes.shawlas.data.table.standard.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Map.Entry;
import java.util.function.BiPredicate;

public interface Shard {

    @NotNull ShardedTable getTable();

    @NotNull String getId();

    /**
     * @return The entry representing the database and table shards
     * */
    @NotNull Entry<@NotNull Database, @NotNull Table> getPartition();

    @Range(from = 1, to = Integer.MAX_VALUE) int getPriority();

    @NotNull BiPredicate<@NotNull Column<?>, @Nullable Object> getPredicate();

    @Override
    int hashCode();

}
package codes.shawlas.data.table.shard;

import codes.shawlas.data.connection.Database;
import codes.shawlas.data.table.standard.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.function.BiPredicate;

public interface Shard {

    @NotNull String getId();

    @NotNull Database.Connection getPartition();

    @Range(from = 1, to = Integer.MAX_VALUE) int getPriority();

    @NotNull BiPredicate<@NotNull Column<?>, @Nullable Object> getPredicate();

    @Override
    int hashCode();

}
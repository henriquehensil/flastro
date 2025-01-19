package codes.shawlas.data.table.index;

import codes.shawlas.data.table.standard.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Iterator;

public interface Index extends Iterable<@NotNull Index> {

    @NotNull IndexedTable getTable();

    @NotNull String getId();

    @Unmodifiable @NotNull Column<?> getColumn();

    @Range(from = 0, to = Long.MAX_VALUE) int size();

    @Override
    @NotNull Iterator<@NotNull Index> iterator();

    @Override
    int hashCode();

}
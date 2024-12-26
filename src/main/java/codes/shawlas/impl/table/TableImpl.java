package codes.shawlas.impl.table;

import codes.shawlas.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TableImpl implements Table {

    private final @NotNull String id;
    private final @NotNull ElementsImpl elements = new ElementsImpl(this);
    private final @NotNull ColumnsImpl columns = new ColumnsImpl(this);

    public TableImpl(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull ElementsImpl getElements() {
        return elements;
    }

    @Override
    public @NotNull ColumnsImpl getColumns() {
        return columns;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Table) {
            final @NotNull Table table = (Table) o;
            return Objects.equals(this.id, table.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

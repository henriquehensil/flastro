package ghostface.dev.impl.table;

import ghostface.dev.impl.database.DatabaseImpl;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TableImpl implements Table {

    private final @NotNull DatabaseImpl database;
    private final @NotNull ElementsImpl datas;
    private final @NotNull ColumnsImpl columns;

    public TableImpl(@NotNull DatabaseImpl database) {
        this.database = database;
        this.datas = new ElementsImpl(this);
        this.columns = new ColumnsImpl(this);
    }

    @Override
    public @NotNull DatabaseImpl getDatabase() {
        return database;
    }

    @Override
    public @NotNull ElementsImpl getElements() {
        return datas;
    }

    @Override
    public @NotNull ColumnsImpl getColumns() {
        return columns;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull TableImpl table = (TableImpl) object;
        return datas.toCollection().equals(table.getElements().toCollection()) && columns.toCollection().equals(table.getColumns().toCollection());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columns);
    }
}
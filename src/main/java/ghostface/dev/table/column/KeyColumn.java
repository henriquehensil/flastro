package ghostface.dev.table.column;

import ghostface.dev.DataType;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class KeyColumn<T> implements Column<T> {

    private final @NotNull Table table;
    private final @NotNull DataType<T> dataType;
    private @NotNull String name;

    public KeyColumn(@NotNull Table table, @NotNull DataType<T> dataType, @NotNull String name) {
        this.table = table;
        this.dataType = dataType;
        this.name = name;
    }

    @Override
    public @NotNull Table getTable() {
        return table;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull DataType<T> getDataType() {
        return dataType;
    }

    @Override
    public final boolean isNullable() {
        return false;
    }

    @Override
    public final @Nullable T getDefault() {
        return null;
    }
}

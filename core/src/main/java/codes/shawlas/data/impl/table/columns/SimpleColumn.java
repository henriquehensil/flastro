package codes.shawlas.data.impl.table.columns;

import codes.shawlas.data.DataType;
import codes.shawlas.data.table.Column;
import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class SimpleColumn<T> implements Column<T> {

    private final @NotNull Table table;

    private @NotNull String name;
    private final @NotNull DataType<T> dataType;
    private final @Nullable T value;
    private final boolean key;
    private final boolean nullable;

    SimpleColumn(@NotNull SimpleColumns columns, @NotNull DataType<T> dataType, @NotNull String name) {
        this.table = columns.getTable();
        this.name = name;
        this.dataType = dataType;
        this.key = true;
        this.nullable = false;
        this.value = null;
    }

    SimpleColumn(
            @NotNull SimpleColumns columns,
            @NotNull DataType<T> dataType,
            @NotNull String name,
            boolean isNullable,
            @Nullable T value
    ) {
        if (!isNullable && value == null) throw new IllegalArgumentException("Non nullable column cannot accept the null value");

        this.table = columns.getTable();
        this.name = name;
        this.dataType = dataType;
        this.key = false;
        this.nullable = isNullable;
        this.value = value;
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
    public @NotNull DataType<T> getDataType() {
        return dataType;
    }

    @Override
    public @Nullable T getDefault() {
        return value;
    }

    @Override
    public boolean isKey() {
        return key;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull Column<?> col) {
        if (key) return !col.isKey() ? -1 : name.compareTo(col.getName());
        else if (nullable) return !col.isNullable() ? 1 : name.compareTo(col.getName());
        else if (col.isKey()) return 1;
        else if (col.isNullable()) return -1;
        else return name.compareTo(col.getName());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Column<?>) {
            @NotNull Column<?> column = (Column<?>) obj;
            return Objects.equals(getTable().getName(), column.getTable().getName()) &&
                    Objects.equals(dataType.getType(), column.getDataType().getType()) &&
                    Objects.equals(name.toLowerCase(), column.getName().toLowerCase()) &&
                    Objects.equals(key, column.isKey()) &&
                    Objects.equals(nullable, column.isNullable());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTable(), getName().toLowerCase(), isKey(), isNullable());
    }
}

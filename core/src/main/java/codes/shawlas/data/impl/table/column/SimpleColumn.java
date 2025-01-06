package codes.shawlas.data.impl.table.column;

import codes.shawlas.data.DataType;
import codes.shawlas.data.table.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class SimpleColumn<T> implements Column<T> {

    private @NotNull String name;
    private final @NotNull DataType<T> dataType;
    private final @Nullable T value;
    private final boolean key;
    private final boolean nullable;

    SimpleColumn(@NotNull DataType<T> dataType, @NotNull String name) {
        this.name = name;
        this.dataType = dataType;
        this.key = true;
        this.nullable = false;
        this.value = null;
    }

    SimpleColumn(
            @NotNull DataType<T> dataType,
            @NotNull String name,
            boolean isNullable,
            @Nullable T value
    ) {
        this.name = name;
        this.dataType = dataType;
        this.key = true;
        this.nullable = isNullable;
        this.value = value;
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
        if (this.isKey()) return col.isKey() ? 0 : -1;
        else if (this.isNullable()) return col.isNullable() ? 0 : 1;
        else return 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Column<?>) {
            @NotNull Column<?> column = (Column<?>) obj;
            return
                    this.getName().equalsIgnoreCase(column.getName()) &&
                    this.isKey() && column.isKey() &&
                    this.isNullable() && column.isNullable();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isKey(), isNullable());
    }
}

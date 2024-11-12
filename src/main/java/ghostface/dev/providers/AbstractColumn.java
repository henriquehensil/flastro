package ghostface.dev.providers;

import ghostface.dev.datatype.DataType;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractColumn<T> implements Column<T> {

    private @NotNull String name;
    private final @NotNull DataType<T> dataType;
    private final boolean isKey;
    private final boolean isNullable;

    protected AbstractColumn(@NotNull String name, @NotNull DataType<T> dataType, boolean isKey, boolean isNullable) {
        if (isKey && isNullable) throw new IllegalArgumentException("Key cannot be nullable");
        this.name = name;
        this.dataType = dataType;
        this.isKey = isKey;
        this.isNullable = isNullable;
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
    public boolean isKey() {
        return isKey;
    }

    @Override
    public boolean isNullable() {
        return isNullable;
    }

    @Override
    public @NotNull String toString() {
        return "AbstractColumn{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", isKey=" + isKey +
                ", isNullable=" + isNullable +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull AbstractColumn<?> that = (AbstractColumn<?>) object;
        return isKey == that.isKey && isNullable == that.isNullable && Objects.equals(name, that.name) && Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataType, isKey, isNullable);
    }
}

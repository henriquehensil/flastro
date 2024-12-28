package codes.shawlas.data.core.impl.table;

import codes.shawlas.data.core.DataType;
import codes.shawlas.data.core.exception.InvalidNameException;
import codes.shawlas.data.core.table.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public final class ColumnImpl<T> implements Column<T> {

    public static @NotNull Pattern regex = Pattern.compile("^[A-Za-z0-9\\s]$");

    private final @NotNull UUID id;
    private @NotNull String name;
    private final @NotNull TableImpl table;
    private final @NotNull DataType<T> dataType;
    private final boolean key;
    private final boolean nullable;
    private final @Nullable T value;

    /**
     * Create a Key Column
     * */
    public ColumnImpl(
            @NotNull UUID id,
            @NotNull String name,
            @NotNull TableImpl table,
            @NotNull DataType<T> dataType
    ) {
        if (!name.matches(regex.pattern())) {
            throw new InvalidNameException("Invalid syntax name");
        }
        this.id = id;
        this.name = name;
        this.table = table;
        this.dataType = dataType;
        this.key = true;
        this.nullable = false;
        this.value = null;
    }

    /**
     * Create a standard Column
     * */
    public ColumnImpl(
            @NotNull UUID id,
            @NotNull String name,
            @NotNull TableImpl table,
            @NotNull DataType<T> dataType,
            @Nullable T value,
            boolean isNullable
    ) {
        if (!name.matches(regex.pattern())) {
            throw new InvalidNameException("Invalid syntax name");
        }
        this.id = id;
        this.name = name;
        this.table = table;
        this.dataType = dataType;
        this.key = false;
        this.value = value;
        this.nullable = isNullable;
    }

    @Override
    public @NotNull TableImpl getTable() {
        return table;
    }

    @Override
    public @NotNull String getId() {
        return id.toString();
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
    public @UnknownNullability T getDefault() {
        return value;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean isKey() {
        return key;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Column<?>) {
            final @NotNull Column<?> column = (Column<?>) o;
            return Objects.equals(getId(), column.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

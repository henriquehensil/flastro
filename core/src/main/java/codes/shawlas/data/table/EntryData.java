package codes.shawlas.data.table;

import codes.shawlas.data.table.standard.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Objects;

public class EntryData<T> implements Serializable {

    private final @NotNull Entry<@NotNull Column<T>, @Nullable T> entry;

    // Constructor

    public EntryData(@NotNull Entry<@NotNull Column<T>, @Nullable T> entry) {
        if (entry.getKey().isKey() && entry.getValue() == null) {
            throw new IllegalArgumentException("Column is key but the value is null");
        } else if (!entry.getKey().isNullable() && entry.getValue() == null) {
            throw new IllegalArgumentException("Column is not nullable but the value is null");
        }

        this.entry = new AbstractMap.SimpleImmutableEntry<>(entry);
    }

    public EntryData(@NotNull Column<T> column, @Nullable T value) {
        if (column.isKey() && value == null) {
            throw new IllegalArgumentException("Column is key but the value is null");
        } else if (!column.isNullable() && value == null) {
            throw new IllegalArgumentException("Column is not nullable but the value is null");
        }

        this.entry = new AbstractMap.SimpleImmutableEntry<>(column, value);
    }

    // Getters

    public @NotNull Column<T> getColumn() {
        return entry.getKey();
    }

    public @Nullable T getValue() {
        return entry.getValue();
    }

    public @Unmodifiable @NotNull Entry<@NotNull Column<T>, @Nullable T> toImutableEntry() {
        return entry;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull EntryData<?> that = (EntryData<?>) object;
        return equals(getColumn(), that.getColumn()) && Objects.equals(getValue(), that.getValue());
    }

    private static boolean equals(@NotNull Column<?> column1, @NotNull Column<?> column2) {
        return column1.getName().equals(column2.getName()) && column1.isKey() == column2.isKey() && column1.isNullable() == column2.isNullable() && column1.getDataType().getType().equals(column2.getDataType().getType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entry);
    }
}
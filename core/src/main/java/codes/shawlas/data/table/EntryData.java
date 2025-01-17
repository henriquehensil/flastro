package codes.shawlas.data.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractMap;
import java.util.Map.Entry;

public class EntryData<T> {

    private final @NotNull Entry<@NotNull Column<T>, @Nullable T> entry;

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
}

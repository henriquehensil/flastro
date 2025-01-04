package codes.shawlas.data.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractMap;
import java.util.Map.Entry;

public class EntryData<T> {

    private final @NotNull Entry<@NotNull Column<T>, @Nullable T> entry;

    public EntryData(@NotNull Entry<@NotNull Column<T>, @Nullable T> entry) {
        this.entry = entry;
    }

    public EntryData(@NotNull Column<T> column, @Nullable T value) {
        this.entry = new AbstractMap.SimpleEntry<>(column, value);
    }

    // Getters

    public @NotNull Column<T> getColumn() {
        return entry.getKey();
    }

    public @Nullable T getValue() {
        return entry.getValue();
    }

    public @Unmodifiable @NotNull Entry<@NotNull Column<T>, @Nullable T> toImutableEntry() {
        return new AbstractMap.SimpleImmutableEntry<>(entry);
    }
}

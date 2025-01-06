package codes.shawlas.data.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Objects;

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

    // Native

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final EntryData<?> entryData = (EntryData<?>) o;
        return Objects.equals(getColumn(), entryData.getColumn()) && Objects.equals(getValue(), entryData.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColumn(), getValue());
    }
}

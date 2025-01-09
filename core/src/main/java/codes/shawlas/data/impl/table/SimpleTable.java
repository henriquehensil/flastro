package codes.shawlas.data.impl.table;

import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class SimpleTable implements Table {

    private final @NotNull String name;
    private final @NotNull SimpleElements elements;
    private final @NotNull SimpleColumns columns;

    SimpleTable(@NotNull String name) {
        this.name = name;

        final @NotNull Object lock = new Object();

        this.elements = new SimpleElements(this, lock);
        this.columns = new SimpleColumns(this, lock);
    }

    // Getters

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull SimpleElements getElements() {
        return elements;
    }

    @Override
    public @NotNull SimpleColumns getColumns() {
        return columns;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Table) {
            final @NotNull Table that = (Table) o;
            return
                    Objects.equals(name.toLowerCase(), that.getName().toLowerCase()) &&
                    Objects.equals(columns.getTable().name.toLowerCase(), that.getColumns().getTable().getName().toLowerCase()) &&
                    Objects.equals(elements.getTable().name.toLowerCase(), that.getElements().getTable().getName().toLowerCase());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toLowerCase());
    }
}
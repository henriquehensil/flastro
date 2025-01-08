package codes.shawlas.data.impl.table;

import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;

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
}
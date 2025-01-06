package codes.shawlas.data.impl.table;

import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.locks.ReentrantLock;

final class SimpleTable implements Table {

    private final @NotNull String name;
    private final @NotNull Elements elements;
    private final @NotNull Columns columns;

    SimpleTable(@NotNull String name) {
        this.name = name;

        try {
            //noinspection unchecked
            @NotNull Class<? extends Elements> elementsRef = (Class<? extends Elements>) Class.forName("codes.shawlas.data.impl.table.element.SimpleElements");
            @NotNull Constructor<? extends Elements> elementsConstructor = elementsRef.getDeclaredConstructor(Table.class, TableLock.class);

            //noinspection unchecked
            @NotNull Class<? extends Columns> columnsRef = (Class<? extends Columns>) Class.forName("codes.shawlas.data.impl.table.column.SimpleColumns");
            @NotNull Constructor<? extends Columns> columnsConstructor = columnsRef.getDeclaredConstructor(Table.class, TableLock.class);

            final @NotNull TableLock lock = new TableLock(new ReentrantLock());

            this.elements = elementsConstructor.newInstance(this, lock);
            this.columns = columnsConstructor.newInstance(this, lock);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the implementation classes", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the implementation constructor ", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot create the implementations",e);
        }
    }

    // Getters

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Elements getElements() {
        return elements;
    }

    @Override
    public @NotNull Columns getColumns() {
        return columns;
    }
}
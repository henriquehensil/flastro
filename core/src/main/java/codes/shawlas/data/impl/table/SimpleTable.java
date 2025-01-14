package codes.shawlas.data.impl.table;

import codes.shawlas.data.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

final class SimpleTable implements Table {

    private final @NotNull String name;
    private final @NotNull Elements elements;
    private final @NotNull Columns columns;

    @SuppressWarnings("unchecked")
    SimpleTable(@NotNull String name) {
        try {
            @NotNull Class<? extends @NotNull Elements> eReference = (Class<? extends Elements>) Class.forName("codes.shawlas.data.impl.table.element.SimpleElements");
            @NotNull Class<? extends @NotNull Columns> cReference = (Class<? extends Columns>) Class.forName("codes.shawlas.data.impl.table.columns.SimpleColumns");

            @NotNull Constructor<? extends @NotNull Elements> eConstructor = eReference.getDeclaredConstructor(Table.class, ReentrantLock.class);
            @NotNull Constructor<? extends @NotNull Columns> cConstructor = cReference.getDeclaredConstructor(Table.class, ReentrantLock.class);

            eConstructor.setAccessible(true);
            cConstructor.setAccessible(true);

            final @NotNull ReentrantLock globalLock = new ReentrantLock();

            this.name = name;
            this.elements = eConstructor.newInstance(this, globalLock);
            this.columns = cConstructor.newInstance(this, globalLock);

            eConstructor.setAccessible(false);
            cConstructor.setAccessible(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the implementations classes", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the constructors of the implementations classes", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementations classes", e);
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

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof Table) {
            final @NotNull Table that = (Table) o;
            return
                    Objects.equals(name.toLowerCase(), that.getName().toLowerCase()) &&
                    Objects.equals(columns, that.getColumns()) &&
                    Objects.equals(elements, that.getElements());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toLowerCase());
    }
}
package ghostface.dev.storage.table;

import ghostface.dev.exception.table.TableException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public interface Table {

    @NotNull String getName();

    @NotNull Optional<? extends Data> getData(int row);

    boolean add(@NotNull Data data) throws TableException;

    boolean remove(@NotNull Data data) throws TableException;

    @Unmodifiable @NotNull Collection<? extends Data> getElements();

    @Unmodifiable @NotNull Collection<? extends Column<?>> getColumns();
}

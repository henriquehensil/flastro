package codes.shawlas.table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public interface TableData<T> {

    @NotNull Column<T> getColumn();

    @UnknownNullability T getValue();
}

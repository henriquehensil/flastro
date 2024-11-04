package ghostface.dev.table.column;

import ghostface.dev.datatype.ConcreteType;
import org.jetbrains.annotations.NotNull;

public interface Column<T> {

    @NotNull String getName();

    @NotNull ConcreteType<T> getConcreteType();

}

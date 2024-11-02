package ghostface.dev.mapping;

import ghostface.dev.datatype.ConcreteDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Column<T> {

    @NotNull String getName();

    boolean isNullable();

    boolean isUnique();

    boolean validate(@Nullable Object value);

    @NotNull ConcreteDataType<T> getDataType();

    @Override
    boolean equals(@Nullable Object o);

    @Override
    int hashCode();
}

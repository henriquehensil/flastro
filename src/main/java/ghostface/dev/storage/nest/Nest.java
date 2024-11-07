package ghostface.dev.storage.nest;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

public interface Nest<T> {

    @NotNull String getUniqueId();

    @NotNull DataType<T> getDataType();

    boolean add(@NotNull String nestName, @NotNull Nest<?> nest);

    boolean addValue(@NotNull T value);

    boolean has(@NotNull String nestName);

    <E> @NotNull Nest<E> get( @NotNull String nestName, @NotNull DataType<E> dataType);

}

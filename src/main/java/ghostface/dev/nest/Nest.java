package ghostface.dev.nest;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Nest<T> {

    @NotNull DataType<T> getDataType();

    <E> @NotNull Nest<E> createSub(@NotNull String name, @NotNull DataType<E> dataType);

    @NotNull Optional<T> getValue(@NotNull String key);

    void putValue(@NotNull String key, @NotNull T object);

    <E> @NotNull Optional<? extends Nest<E>> getSub(@NotNull String name, @NotNull DataType<E> dataType);

    void putSub(@NotNull String name, @NotNull Nest<?> nest);

}

package ghostface.dev.nest;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExists;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Nest<T> {

    /**
     * @throws NameAlreadyExists if the ID already in use
     * */
    <E> @NotNull Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NameAlreadyExists;

    boolean put(@NotNull String key, T value);

    // Getters

    <E> @NotNull Optional<? extends Nest<E>> getSub(@NotNull String id, @NotNull DataType<E> dataType);

    @NotNull Optional<T> getValue(@NotNull String key);

    @NotNull DataType<T> getDataType();
}

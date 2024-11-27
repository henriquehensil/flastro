package ghostface.dev.nest;

import ghostface.dev.DataType;
import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.exception.NameAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Nest<T> extends UnmodifiableContent<T> {

    /**
     * @throws NameAlreadyExistsException if the ID already in use
     * */
    <E> @NotNull Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NameAlreadyExistsException;

    boolean put(@NotNull String key, T value);

    // Getters

    <E> @NotNull Optional<? extends Nest<E>> getSub(@NotNull String id, @NotNull DataType<E> dataType);

    @NotNull Optional<T> getValue(@NotNull String key);

    @NotNull DataType<T> getDataType();

    @Override
    @Unmodifiable @NotNull Collection<? extends T> toCollection();
}

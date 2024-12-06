package ghostface.dev.nest;

import ghostface.dev.DataType;
import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.exception.nest.NestAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Nest<T> extends UnmodifiableContent<T> {

    @NotNull NestStorage getStorage();

    @NotNull Optional<T> getValue(@NotNull String key);

    boolean put(@NotNull String key, T value);

    @NotNull DataType<T> getDataType();

    @Override
    @Unmodifiable @NotNull Collection<T> toCollection();

    // Classes

    interface Children extends UnmodifiableContent<@NotNull Nest<?>> {

        /**
         * @throws NestAlreadyExistsException if the ID already in use
         * */
        <E> @NotNull Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NestAlreadyExistsException;

        @NotNull Optional<? extends @NotNull Nest<?>> getSub(@NotNull String id);

        @Override
        @Unmodifiable @NotNull Collection<@NotNull Nest<?>> toCollection();

    }
}

package codes.shawlas.nest;

import codes.shawlas.DataType;
import codes.shawlas.content.UnmodifiableContent;
import codes.shawlas.exception.nest.NestAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Nest<T> {

    @NotNull NestStorage getStorage();

    @NotNull Children getSubs();

    @NotNull Optional<T> getValue(@NotNull String key);

    boolean put(@NotNull String key, T value);

    @NotNull DataType<T> getDataType();

    @Unmodifiable @NotNull Collection<T> getValues();

    // Classes

    interface Children extends UnmodifiableContent<@NotNull Nest<?>> {

        /**
         * @throws NestAlreadyExistsException if the ID already in use
         * */
        <E> @NotNull Nest<E> createSub(@NotNull String id, @NotNull DataType<E> dataType) throws NestAlreadyExistsException;

        @NotNull Optional<? extends @NotNull Nest<?>> getSub(@NotNull String id);

        boolean delete(@NotNull String id);

        @Override
        @Unmodifiable @NotNull Collection<@NotNull Nest<?>> toCollection();

    }
}

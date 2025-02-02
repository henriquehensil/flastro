package codes.shawlas.data.nest;

import codes.shawlas.data.io.DataType;
import codes.shawlas.data.exception.nest.NestAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage {

    @NotNull Nests getNests();

    interface Nests extends Iterable<@NotNull Nest<?>> {

        /**
         * @throws NestAlreadyExistsException if table {@code name} is already in use
         * */
        <E> @NotNull Nest<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws NestAlreadyExistsException;

        boolean remove(@NotNull String name);

        @NotNull Optional<? extends @NotNull Nest<?>> get(@NotNull String name);

        @Unmodifiable @NotNull Collection<? extends @NotNull Nest<?>> getAll();

    }

}

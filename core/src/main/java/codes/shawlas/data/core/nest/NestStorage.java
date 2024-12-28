package codes.shawlas.data.core.nest;

import codes.shawlas.data.core.DataType;
import codes.shawlas.data.core.database.Database;
import codes.shawlas.data.core.exception.nest.NestAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage {

    @NotNull Database database();

    @NotNull Nests getNests();

    // Classes

    interface Nests {

        /**
         * @throws NestAlreadyExistsException if {@code name} is already in use
         * */
        <E> @NotNull Nest<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws NestAlreadyExistsException;

        @NotNull Optional<? extends @NotNull Nest<?>> get(@NotNull String name);

        @NotNull Optional<? extends @NotNull Nest<?>> getById(@NotNull String id);

        boolean delete(@NotNull String name);

        boolean deleteById(@NotNull String name);

        @Unmodifiable @NotNull Collection<@NotNull Nest<?>> toCollection();
    }
}

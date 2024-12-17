package codes.shawlas.nest;

import codes.shawlas.DataType;
import codes.shawlas.content.NamedContent;
import codes.shawlas.database.Database;
import codes.shawlas.exception.NameAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage {

    @NotNull Database database();

    @NotNull Nests getNests();

    // Classes

    interface Nests extends NamedContent<@NotNull Nest<?>>  {

        @NotNull NestStorage getNestStorage();

        /**
         * @throws NameAlreadyExistsException if {@code name} is already in use
         * */
        <E> @NotNull Nest<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws NameAlreadyExistsException;

        // Implementations

        @Override
        @NotNull Optional<? extends @NotNull Nest<?>> get(@NotNull String name);

        @Override
        boolean delete(@NotNull String name);

        @Override
        @Unmodifiable
        @NotNull Collection<@NotNull Nest<?>> toCollection();
    }
}

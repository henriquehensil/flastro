package codes.shawlas.data.nest;

import codes.shawlas.data.DataType;
import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage {

    @NotNull Nests getNests();

    interface Nests extends Iterable<@NotNull Nest<?>> {

        /**
         * @throws TableAlreadyExistsException if table {@code name} is already in use
         * */
        <E> @NotNull Nest<E> create(@NotNull String name, @NotNull DataType<E> dataType) throws TableAlreadyExistsException;

        boolean remove(@NotNull String name);

        @NotNull Optional<@NotNull Nest<?>> get(@NotNull String name);

        @Unmodifiable @NotNull Collection<? extends @NotNull Nest<?>> getAll();

    }

}

package ghostface.dev.storage;

import ghostface.dev.DataType;
import ghostface.dev.content.NamedContent;
import ghostface.dev.database.Database;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface NestStorage extends NamedContent<Nest<?>> {

    @NotNull Database database();

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
    @Unmodifiable @NotNull Collection<? extends @NotNull Nest<?>> toCollection();
}

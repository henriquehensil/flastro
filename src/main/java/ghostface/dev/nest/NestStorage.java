package ghostface.dev.nest;

import ghostface.dev.datatype.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface NestStorage {

    <E> @NotNull Optional<? extends Nest<E>> get(@NotNull String name, DataType<E> dataType);

    boolean add(@NotNull String name, @NotNull Nest<?> nest);

    boolean remove(@NotNull String name, @NotNull Nest<?> nest);

}

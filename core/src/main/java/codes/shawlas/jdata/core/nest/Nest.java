package codes.shawlas.jdata.core.nest;

import codes.shawlas.jdata.core.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Nest<T> {

    @Nullable Nest<?> getFather();

    @NotNull String getId();

    @NotNull Optional<T> getValue(@NotNull String key);

    boolean put(@NotNull String key, T value);

    @NotNull SubNests getSubs();

    @NotNull DataType<T> getDataType();

    @Unmodifiable @NotNull Collection<T> getValues();

    /**
     * An Interface that encapsulates sub nests
     * */
    interface SubNests extends NestStorage.Nests {

        default boolean exists(@NotNull String id) {
            return getById(id).isPresent();
        }
    }
}

package ghostface.dev.table.data;

import ghostface.dev.exception.table.DuplicatedKeyException;
import ghostface.dev.exception.table.MissingKeyException;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Datas {

    @NotNull Table getTable();

    @NotNull Data create(@NotNull Object @NotNull ... key) throws MissingKeyException, DuplicatedKeyException;

    boolean remove(int index);

    @NotNull Optional<? extends Data> get(int index);

    @Unmodifiable @NotNull Collection<? extends Data> toCollection();

}

package ghostface.dev.storage.table.data;

import ghostface.dev.exception.table.DuplicatedKeyException;
import ghostface.dev.exception.table.MissingKeyException;
import ghostface.dev.storage.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Datas {

    @NotNull Table getTable();

    @NotNull Data create(@NotNull Object @NotNull ... key) throws MissingKeyException, DuplicatedKeyException;

    @NotNull Data delete(int index);

    @NotNull Optional<Data> get(int index);

    @Unmodifiable @NotNull Collection<Data> toCollection();

}

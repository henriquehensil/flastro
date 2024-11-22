package ghostface.dev.table.data;

import ghostface.dev.exception.column.DuplicatedColumnException;
import ghostface.dev.exception.key.DuplicatedKeyValueException;
import ghostface.dev.exception.key.MissingKeyException;
import ghostface.dev.table.Key;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Elements {

    /**
     * @throws MissingKeyException if the key is missing
     * @throws DuplicatedKeyValueException if the key value already exists
     * @throws DuplicatedColumnException if the keys contain duplicated columns.
     * */
    @NotNull Data create(@NotNull Key<?> @NotNull ... keys) throws MissingKeyException, DuplicatedKeyValueException, DuplicatedColumnException;

    boolean remove(int index);

    // Getters

    @NotNull Table getTable();

    @NotNull Optional<? extends Data> get(int index);

    @Unmodifiable @NotNull Collection<? extends Data> toCollection();
}

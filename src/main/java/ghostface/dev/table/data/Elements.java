package ghostface.dev.table.data;

import ghostface.dev.exception.column.DuplicatedColumnException;
import ghostface.dev.exception.key.DuplicatedKeyException;
import ghostface.dev.exception.key.MissingKeyException;
import ghostface.dev.exception.table.TableException;
import ghostface.dev.table.Key;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Elements {

    /**
     * @throws MissingKeyException if the key is missing
     * @throws DuplicatedKeyException if the key value already exists
     * @throws TableException if the key's column does not belong to the table.
     * @throws DuplicatedColumnException if the keys contain duplicate columns.
     * */
    @NotNull Data create(@NotNull Key<?> @NotNull ... keys) throws MissingKeyException, DuplicatedKeyException, DuplicatedColumnException, TableException;

    boolean remove(int index);

    // Getters

    @NotNull Table getTable();

    @NotNull Optional<? extends Data> get(int index);

    @Unmodifiable @NotNull Collection<? extends Data> toCollection();
}

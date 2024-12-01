package ghostface.dev.table.data;

import ghostface.dev.content.UnmodifiableContent;
import ghostface.dev.exception.key.DuplicatedKeyValueException;
import ghostface.dev.exception.key.MissingKeyException;
import ghostface.dev.exception.table.TableStateException;
import ghostface.dev.table.Data;
import ghostface.dev.table.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;

public interface Elements extends UnmodifiableContent<@NotNull Element> {

    /**
     * @throws MissingKeyException If the key is missing
     * @throws DuplicatedKeyValueException If the key value already exists
     * @throws TableStateException If table#getColumns is empty
     * */
    @NotNull Element create(@NotNull Data<?> @NotNull ... data) throws MissingKeyException, DuplicatedKeyValueException, TableStateException;

    boolean remove(int index);

    // Getters

    @NotNull Table getTable();

    @NotNull Optional<? extends Element> get(int index);

    @Override
    @Unmodifiable @NotNull Collection<@NotNull Element> toCollection();
}
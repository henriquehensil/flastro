package codes.shawlas.jdata.core.file;

import codes.shawlas.jdata.core.database.Database;
import codes.shawlas.jdata.core.exception.file.FileAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public interface FileStorage {

    @NotNull Database database();

    @NotNull Path getDefault();

    @NotNull Storages getFiles();

    // Classes

    interface Storages extends Iterable<@NotNull MetaFile> {

        /**
         * Create an empty file
         *
         * @throws FileAlreadyExistsException if {@code name} is already in use
         * @throws IOException if an I/O error occurs
         * */
        @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException;

        /**
         * Create an empty file into directory
         *
         * @throws FileAlreadyExistsException if {@code name} is already in use in the directory
         * @throws IOException if an I/O error occurs
         * */
        @NotNull MetaFile create(@NotNull String directory, @NotNull String name) throws FileAlreadyExistsException, IOException;

        /**
         * Stores an uploaded file
         *
         * @throws UnsupportedOperationException if file path is incorrect. this includes it being a directory
         * @throws FileAlreadyExistsException if file name is already stored in this path
         * @throws IOException if an I/O error occurs
         * */
        @NotNull MetaFile store(@NotNull File file) throws FileAlreadyExistsException, IOException;

        boolean delete(@NotNull Path path);

        @NotNull Optional<? extends @NotNull MetaFile> get(@NotNull Path path);

        @Unmodifiable @NotNull Collection<? extends @NotNull MetaFile> toCollection();
    }
}

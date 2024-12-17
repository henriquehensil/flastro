package codes.shawlas.file;

import codes.shawlas.content.NamedContent;
import codes.shawlas.database.Database;
import codes.shawlas.exception.file.FileAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public interface FileStorage {

    @NotNull Database database();

    @NotNull Path getDefault();

    @NotNull Files getFiles();

    // Classes

    interface Files extends NamedContent<@NotNull MetaFile> {

        @NotNull FileStorage getFileStorage();

        /**
         * @throws FileAlreadyExistsException if {@code name} is already in use
         * @throws IOException if an I/O error occurs
         * */
        @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException;

        /**
         * @throws FileAlreadyExistsException if {@code folder} and {@code name} is already in use
         * @throws IOException if an I/O error occurs
         * */
        @NotNull MetaFile create(@NotNull String folder, @NotNull String name) throws FileAlreadyExistsException, IOException;

        @NotNull Optional<@NotNull MetaFile> get(@NotNull Path path);

        boolean delete(@NotNull Path path);

        // Implementations

        @Override
        @NotNull Optional<@NotNull MetaFile> get(@NotNull String name);

        @Override
        boolean delete(@NotNull String name);

        @Override
        @Unmodifiable @NotNull Collection<@NotNull MetaFile> toCollection();
    }
}

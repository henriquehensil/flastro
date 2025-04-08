package dev.hensil.flastro.core.file;

import dev.hensil.flastro.core.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import java.io.IOException;
import java.util.Optional;

public interface FileStorage extends Storage {

    @NotNull Manager getManager();

    // Classes

    interface Manager {

        // Objects

        default @NotNull Path getRootPath() {
            return Paths.get("/");
        }

        @NotNull MetaFile createFile(@NotNull Path parents, @NotNull String name, boolean mkdirs) throws IOException;

        @NotNull MetaFile createFolder(@NotNull Path parents, @NotNull String name, boolean mkdirs) throws IOException;

        @NotNull Optional<@NotNull MetaFile> get(@NotNull URI uri);

        /**
         * @throws java.nio.file.NoSuchFileException if the file doest not exists.
         * @throws java.nio.file.NotDirectoryException if the file exists, but is a folder.
         * @throws IOException if another error occurs.
         * */
        void deleteFile(@NotNull URI uri) throws IOException;

        /**
         * @throws java.nio.file.NoSuchFileException if the folder doest not exists.
         * @throws java.nio.file.NotDirectoryException if is not a folder.
         * @throws IOException if another error occurs.
         * */
        void deleteFolder(@NotNull URI uri, boolean onlyIfIsEmpty) throws IOException;

        void move(@NotNull URI uri, @NotNull Path target, boolean mkdirs) throws IOException;

        void copy(@NotNull URI uri, @NotNull Path target, boolean mkdirs) throws IOException;

        void rename(@NotNull URI uri, @NotNull String newName) throws IOException;

        @NotNull Collection<? extends @NotNull MetaFile> getFiles(@NotNull Path location) throws IOException;

    }
}
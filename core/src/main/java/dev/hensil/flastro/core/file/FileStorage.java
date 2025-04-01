package dev.hensil.flastro.core.file;

import dev.hensil.flastro.core.exception.file.FileStorageException;
import dev.hensil.flastro.core.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Predicate;

public interface FileStorage extends Storage {

    @NotNull Path getRootLocation();

    @NotNull Manager getManager();

    @NotNull Writer getWriter(@NotNull Path path, boolean append);

    @NotNull Reader getReader(@NotNull Path path);

    // Classes

    interface Manager {

        @NotNull MetaFile createFile(@NotNull Path location, @NotNull String name, boolean mkdirs) throws FileStorageException;

        @NotNull MetaFile createFolder(@NotNull Path location, @NotNull String name, boolean mkdirs) throws FileStorageException;

        @NotNull MetaFile get(@NotNull Path path) throws FileStorageException;

        boolean delete(@NotNull Path path);

        boolean deleteAll(@NotNull Path @NotNull ... paths);

        boolean deleteAll(@NotNull Path path);

        boolean deleteIf(@NotNull Path location, @NotNull Predicate<? extends @NotNull MetaFile> predicate);

        @NotNull Collection<? extends @NotNull MetaFile> getFiles(@NotNull Path location) throws FileStorageException;

        void move(@NotNull Path location, @NotNull Path target) throws FileStorageException;

        void copy(@NotNull Path location, @NotNull Path target) throws FileStorageException;

        void rename(@NotNull Path path, @NotNull String newName) throws FileStorageException;

        boolean contains(@NotNull Path path);

    }

    interface Writer {

        int write(@NotNull ByteBuffer buffer) throws FileStorageException;

    }

    interface Reader {

        int read(@NotNull ByteBuffer buffer) throws FileStorageException;

    }
}
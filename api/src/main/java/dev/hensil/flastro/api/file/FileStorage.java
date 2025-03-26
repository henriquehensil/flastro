package dev.hensil.flastro.api.file;

import dev.hensil.flastro.api.exception.FileStorageException;
import dev.hensil.flastro.api.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.file.Path;

public interface FileStorage extends Storage {

    @NotNull Path getRoot();

    @NotNull Manager getManager();

    @NotNull Data getData();

    // Classes

    interface Manager {

        @NotNull MetaFile createFile(@NotNull Path path) throws FileStorageException;

        @NotNull MetaFile createFolder(@NotNull Path path) throws FileStorageException;

        void move(@NotNull Path path, @NotNull Path target) throws FileStorageException;

        void copy(@NotNull Path path, @NotNull Path target) throws FileStorageException;

        @NotNull MetaFile get(@NotNull Path path) throws FileStorageException;

        boolean delete(@NotNull Path path);

        boolean contains(@NotNull Path path);

    }

    interface Data {

        int read(@NotNull Path path, @NotNull ByteBuffer buffer) throws FileStorageException;

        void write(@NotNull Path path, @NotNull ByteBuffer buffer) throws FileStorageException;

        boolean decompress(@NotNull Path path);

        boolean compress(@NotNull Path path);

        void clear(@NotNull Path path);

    }
}

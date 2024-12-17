package codes.shawlas.impl.core;

import codes.shawlas.database.Database;
import codes.shawlas.exception.file.FileAlreadyExistsException;
import codes.shawlas.file.FileStorage;
import codes.shawlas.file.MetaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class FileStorageImpl implements FileStorage {

    private final @NotNull Database database;
    private final @NotNull Path path;

    FileStorageImpl(@NotNull Database database, @NotNull Path path) {
        this.database = database;
        this.path = path;
    }

    @Override
    public @NotNull Database database() {
        return database;
    }

    @Override
    public @NotNull Path getDefault() {
        return path;
    }

    @Override
    public @NotNull Files getFiles() {
        return null;
    }

    public final class FilesImpl implements Files {

        private final @NotNull Map<@NotNull Path, @NotNull MetaFile> fileMap = new HashMap<>();

        @Override
        public @NotNull FileStorageImpl getFileStorage() {
            return FileStorageImpl.this;
        }

        @Override
        public @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException {
            return null;
        }

        @Override
        public @NotNull MetaFile create(@NotNull String folder, @NotNull String name) throws FileAlreadyExistsException, IOException {
            return null;
        }

        @Override
        public @NotNull Optional<@NotNull MetaFile> get(@NotNull Path path) {
            return Optional.empty();
        }

        @Override
        public boolean delete(@NotNull Path path) {
            return false;
        }

        @Override
        public @NotNull Optional<@NotNull MetaFile> get(@NotNull String name) {
            return Optional.empty();
        }

        @Override
        public boolean delete(@NotNull String name) {
            return false;
        }

        @Override
        public @Unmodifiable @NotNull Collection<@NotNull MetaFile> toCollection() {
            return fileMap.values();
        }
    }
}
package codes.shawlas.data.core.impl.core;

import codes.shawlas.data.core.database.Database;
import codes.shawlas.data.core.exception.file.FileAlreadyExistsException;
import codes.shawlas.data.core.file.FileStorage;
import codes.shawlas.data.core.file.MetaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public final class FileStorageImpl implements FileStorage {

    // Objects
    private final @NotNull Object lock = new Object();
    private final @NotNull Database database;
    private final @NotNull Path folder;
    private final @NotNull StoragesImpl files = new StoragesImpl();

    FileStorageImpl(@NotNull Database database, @NotNull Path folder) {
        this.database = database;
        this.folder = folder;
        try {
            Files.createDirectories(folder);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create folder: " + e);
        }
    }

    @Override
    public @NotNull Database database() {
        return database;
    }

    @Override
    public @NotNull Path getDefault() {
        return folder;
    }

    @Override
    public @NotNull StoragesImpl getFiles() {
        return files;
    }

    // Classes
    public final class StoragesImpl implements Storages {

        private final @NotNull Map<@NotNull Path, @NotNull MetaFile> fileMap = new HashMap<>();

        /**
         * @throws InvalidPathException if {@code name} is an invalid syntax.
         * @throws FileAlreadyExistsException if {@code name} is already in use
         * @throws IOException if an I/O error occurs
         * */
        @Override
        public @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException, InvalidPathException {
            return finish(null, name);
        }

        /**
         * @throws InvalidPathException if {@code folder} or {@code name} is an invalid syntax.
         * @throws FileAlreadyExistsException if {@code name} is already being used in the last abstract path
         * @throws IOException if an I/O error occurs
         * */
        @Override
        public @NotNull MetaFile create(@NotNull String directory, @NotNull String name) throws FileAlreadyExistsException, IOException, InvalidPathException {
            return finish(directory, name);
        }

        /**
         * Helper method that creates a file and places it in the fileMap
         * */
        private @NotNull MetaFile finish(@Nullable String folder, @NotNull String name) throws IOException {
            if (name.trim().isEmpty()) {
                throw new InvalidPathException("cannot be null", "The name");
            }

            @NotNull Path path;
            if (folder != null) {
                path = getDefault().resolve(Paths.get(folder, name));
            } else {
                path = getDefault().resolve(name);
            }

            @NotNull File file = path.getParent().toFile();

            synchronized (lock) {
                if (!file.exists() && !file.mkdirs()) {
                    throw new RuntimeException("Cannot create folder: " + file.getAbsolutePath());
                }

                file = path.toFile();
                if (get(path).isPresent() || file.exists()) {
                    throw new FileAlreadyExistsException("The file name is already in use: " + path);
                } else if (!file.createNewFile()) {
                    throw new RuntimeException("Cannot create file: " + file.getAbsolutePath());
                }

                @NotNull MetaFile metaFile = MetaFile.create(file);

                fileMap.put(metaFile.getPath(), metaFile);
                return metaFile;
            }
        }

        /**
         * Stores an uploaded file
         *
         * @throws UnsupportedOperationException if file path is a directory
         * @throws FileAlreadyExistsException if file name is already stored in this path
         * @throws IOException if an I/O error occurs
         * @throws RuntimeException if an Fatal error occurs
         * */
        @Override
        public @NotNull MetaFile store(@NotNull File file) throws FileAlreadyExistsException, IOException {
            if (file.isDirectory()) {
                throw new UnsupportedOperationException("File is an directory");
            } else synchronized (lock) {
                file = !file.toPath().startsWith(getDefault()) ? getDefault().resolve(file.toPath()).toFile() : file;

                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new RuntimeException("Cannot create folder: " + file.getAbsolutePath());
                } else if (!file.exists() && !file.createNewFile()) {
                    throw new RuntimeException("Cannot create file: " + file.getAbsolutePath());
                } else {
                    @NotNull MetaFile metaFile = MetaFile.create(file);
                    fileMap.put(metaFile.getPath(), metaFile);
                    return metaFile;
                }
            }
        }

        @Override
        public @NotNull Optional<@NotNull MetaFile> get(@NotNull Path path) {
            return Optional.ofNullable(fileMap.get(path.startsWith(getDefault()) ? path : getDefault().resolve(path)));
        }

        @Override
        public boolean delete(@NotNull Path path) {
            path = path.startsWith(getDefault()) ? path : getDefault().resolve(path);
            if (path.equals(getDefault())) {
                return false;
            } else synchronized (lock) {
                try {
                    Files.delete(path);
                    fileMap.remove(path);
                    return true;
                } catch (Throwable e) {
                    return false;
                }
            }
        }

        @Override
        public @Unmodifiable @NotNull Set<@NotNull MetaFile> toCollection() {
            return Collections.unmodifiableSet(new HashSet<>(fileMap.values()));
        }

        @Override
        public @NotNull Iterator<@NotNull MetaFile> iterator() {
            return toCollection().iterator();
        }
    }
}
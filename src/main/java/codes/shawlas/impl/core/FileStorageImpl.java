package codes.shawlas.impl.core;

import codes.shawlas.database.Database;
import codes.shawlas.exception.file.FileAlreadyExistsException;
import codes.shawlas.file.FileStorage;
import codes.shawlas.file.MetaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public final class FileStorageImpl implements FileStorage {

    public static @NotNull Pattern nameRegex = Pattern.compile("^[A-Za-z0-9-_.]{2,63}$");
    public static @NotNull Pattern folderRegex = Pattern.compile("^[^:?\"<>|]*$");

    // Objects
    private final @NotNull Object lock = new Object();
    private final @NotNull Database database;
    private final @NotNull Path path;
    private final @NotNull Storages files = new StoragesImpl();

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
    public @NotNull Storages getFiles() {
        return files;
    }

    public final class StoragesImpl implements Storages {

        private final @NotNull Map<@NotNull Path, @NotNull MetaFile> fileMap = new HashMap<>();

        /**
         * @throws IllegalArgumentException if {@code name} is an invalid syntax.
         * @throws FileAlreadyExistsException if {@code name} is already in use
         * @throws IOException if an I/O error occurs
         * */
        @Override
        public @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException, IllegalArgumentException {
            return finish(null, name);
        }

        /**
         * @throws IllegalArgumentException if {@code folder} or {@code name} is an invalid syntax.
         * @throws FileAlreadyExistsException if {@code name} is already being used in the last abstract path
         * @throws IOException if an I/O error occurs
         * */
        @Override
        public @NotNull MetaFile create(@NotNull String directory, @NotNull String name) throws FileAlreadyExistsException, IOException {
            return finish(directory, name);
        }

        /**
         * Helper method that creates a file and places it in the fileMap
         * */
        private @NotNull MetaFile finish(@Nullable String folder, @NotNull String name) throws IOException {
            if (!name.matches(nameRegex.pattern())) {
                throw new IllegalArgumentException("Invalid name syntax");
            }

            @NotNull Path path;
            if (folder != null) {
                if (folder.matches(".*[:?\"<>|].*")) {
                    throw new IllegalArgumentException("Invalid folder syntax");
                } else {
                    path = getDefault().resolve(Paths.get(folder, name));
                }
            } else {
                path = getDefault().resolve(name);
            }

            synchronized (lock) {
                if (folder != null && !Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                if (Files.exists(path)) {
                    throw new FileAlreadyExistsException("The file name is already in use: " + path);
                }

                Files.createFile(path);

                @NotNull MetaFile metaFile = MetaFile.create(path.toFile());

                fileMap.put(metaFile.getPath(), metaFile);
                return metaFile;
            }
        }

        @Override
        public @NotNull MetaFile store(@NotNull File file) throws FileAlreadyExistsException, IOException {
            if (!file.toPath().startsWith(getDefault())) {
                throw new UnsupportedOperationException("File path is invalid");
            } else if (file.isDirectory()) {
                throw new UnsupportedOperationException("File is an directory");
            } else synchronized (lock) {
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
            return fileMap.remove(path.startsWith(getDefault()) ? path : getDefault().resolve(path)) != null;
        }

        @Override
        public @Unmodifiable @NotNull Collection<@NotNull MetaFile> toCollection() {
            return fileMap.values();
        }

        @Override
        public @NotNull Iterator<@NotNull MetaFile> iterator() {
            return toCollection().iterator();
        }
    }
}
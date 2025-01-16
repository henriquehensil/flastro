package codes.shawlas.data.impl.file;

import codes.shawlas.data.exception.file.FileAlreadyExistsException;
import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.file.MetaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

final class SimpleFileStorage implements FileStorage {

    private final @NotNull MetaFiles files = new Metas();
    private final @NotNull Path root;

    private SimpleFileStorage(@NotNull Path root) {
        this.root = root;
    }

    @Override
    public @NotNull Path getRoot() {
        return root;
    }

    @Override
    public @NotNull MetaFiles getFiles() {
        return files;
    }

    // Classes

    private final class Metas implements MetaFiles {

        private final @NotNull Map<@NotNull Path, @NotNull MetaFile> fileMap = new HashMap<>();

        @Override
        public @NotNull MetaFile create(@NotNull String name) throws FileAlreadyExistsException, IOException {
            return finish(null, name);
        }

        @Override
        public @NotNull MetaFile create(@NotNull String directory, @NotNull String name) throws FileAlreadyExistsException, IOException {
            return finish(directory, name);
        }

        private @NotNull MetaFile finish(@Nullable String directory, @NotNull String name) throws FileAlreadyExistsException, IOException, InvalidPathException {
            if (name.contains("/") || name.contains("\0")) {
                throw new IllegalArgumentException("File name cannot contains the \"/\" character");
            }

            @NotNull Path path = directory != null ? root.resolve(Paths.get(directory, name)) : root.resolve(Paths.get(name));

            synchronized (this) {
                if (fileMap.containsKey(path)) {
                    throw new FileAlreadyExistsException("The file '" + name +"' is already in use");
                }

                Files.createFile(path);
                @NotNull MetaFile file = MetaFile.create(path.toFile());
                fileMap.put(path, file);
                return file;
            }
        }

        @Override
        public @NotNull MetaFile store(@NotNull File file) throws FileAlreadyExistsException, IOException, InvalidPathException {
            synchronized (this) {
                if (fileMap.containsKey(file.toPath())) {
                    throw new FileAlreadyExistsException("The file '" + file.toPath() +"' is already in use");
                } else if (!file.exists() && !file.createNewFile()) {
                    throw new RuntimeException("Cannot create the file: " + file.getAbsolutePath());
                } else {
                    @NotNull MetaFile metaFile = MetaFile.create(file);
                    fileMap.put(metaFile.getPath(), metaFile);
                    return metaFile;
                }
            }
        }

        @Override
        public boolean delete(@NotNull Path path) {
            path = path.startsWith(root) ? path : root.resolve(path);
            synchronized (this) {
                return fileMap.remove(path) != null;
            }
        }

        @Override
        public @NotNull Optional<@NotNull MetaFile> get(@NotNull Path path) {
            path = path.startsWith(root) ? path : root.resolve(path);
            synchronized (this) {
                return Optional.ofNullable(fileMap.get(path));
            }
        }

        @Override
        public @Unmodifiable @NotNull Collection<@NotNull MetaFile> toCollection() {
            synchronized (this) {
                return fileMap.values();
            }
        }

        @Override
        public @NotNull Iterator<@NotNull MetaFile> iterator() {
            return toCollection().iterator();
        }
    }
}
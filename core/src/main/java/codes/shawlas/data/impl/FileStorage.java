package codes.shawlas.data.impl;

import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class FileStorage implements Storage {

    private final @NotNull FileManager manager = new FileManager();
    private final @NotNull Path root;

    public FileStorage(@NotNull Path root) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        this.root = root;
    }

    @Override
    public @NotNull String getId() {
        return "#file_storage";
    }

    public @NotNull Path getRoot() {
        return root;
    }

    public @NotNull FileManager getManager() {
        return manager;
    }

    // Classes

    public final class FileManager {

        private final @NotNull Set<@NotNull Path> paths = new HashSet<>();

        public @NotNull File create(@NotNull Path folders, @NotNull String name, @NotNull InputStream data) throws IOException {
            folders = folders.startsWith(root) ? folders.resolve(name) : root.resolve(folders.resolve(name));

            if (contains(folders)) {
                throw new FileAlreadyExistsException("The file already exists: " + folders);
            }

            if (!Files.exists(folders.getParent())) {
                Files.createDirectory(folders.getParent());
            }

            Files.createFile(folders);

            if (Files.isDirectory(folders)) {
                throw new IllegalArgumentException("The path already exists, but the " + name + " is not a file: " + folders);
            }

            final @NotNull File file = folders.toFile();
            write(file, data);
            paths.add(folders);

            return file;
        }

        private void write(@NotNull File file, @NotNull InputStream inputStream) throws IOException {
            if (file.isDirectory()) {
                throw new IllegalArgumentException("File must to be a regular file: " + file.getAbsolutePath());
            } else try (@NotNull FileOutputStream output = new FileOutputStream(file)) {
                byte @NotNull [] bytes = new byte[8192]; // 8kb

                int read;
                while ((read = inputStream.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                    output.flush();
                }
            }
        }

        public boolean contains(@NotNull Path path) {
            return paths.contains(path.startsWith(root) ? path : root.resolve(path));
        }

        public boolean delete(@NotNull Path path) throws IOException {
            path = path.startsWith(root) ? path : root.resolve(path);

            if (!contains(path)) {
                return false;
            } else {
                Files.delete(path);
                return true;
            }
        }
    }
}
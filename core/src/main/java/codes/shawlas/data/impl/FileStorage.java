package codes.shawlas.data.impl;

import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class FileStorage implements Storage {

    private final @NotNull FileManager manager = new FileManager();
    private final @NotNull Path root;

    public FileStorage(@NotNull Path root) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        this.root = root;
    }

    @Override
    public final @NotNull String getId() {
        return "#file_storage";
    }

    public final @NotNull Path getRoot() {
        return root;
    }

    public @NotNull FileManager getManager() {
        return manager;
    }

    // Classes

    public class FileManager {

        private final @NotNull Set<@NotNull Path> paths = new HashSet<>();

        public @NotNull File create(@NotNull String folders, @NotNull String name, @NotNull InputStream data) throws IOException {
            @NotNull Path path = Paths.get(folders, name);
            path = path.startsWith(root) ? path : root.resolve(path);

            if (contains(path)) {
                throw new FileAlreadyExistsException("The file already exists: " + folders);
            }

            if (!Files.exists(path.getParent())) {
                Files.createDirectory(path.getParent());
            }

            Files.createFile(path);

            if (Files.isDirectory(path)) {
                throw new IllegalArgumentException("The path already exists, but the " + name + " is not a file: " + path);
            }

            final @NotNull File file = path.toFile();
            write(file, data);
            paths.add(path);

            System.out.println("create: " + path);

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

        public boolean contains(@NotNull String path) {
            try {
                return contains(Paths.get(path));
            } catch (InvalidPathException e) {
                return false;
            }
        }

        public final boolean contains(@NotNull Path path) {
            return paths.contains(path.startsWith(root) ? path : root.resolve(path));
        }

        public boolean delete(@NotNull String path) throws IOException {
            final @NotNull Path p = path.startsWith(root.toString()) ? Paths.get(path) : root.resolve(path);

            System.out.println(p);

            paths.remove(p);
            return Files.deleteIfExists(p);
        }
    }
}
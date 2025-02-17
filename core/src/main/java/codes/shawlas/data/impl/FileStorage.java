package codes.shawlas.data.impl;

import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileStorage implements Storage {

    private final @NotNull FileManager manager = new FileManager();
    private final @NotNull Path root;
    private final @NotNull Object lock = new Object();

    public FileStorage(@NotNull Path root) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
            Files.createDirectory(root.resolve("upload"));
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

        public @NotNull FileOutputStream createFile(@NotNull Path path) throws IOException {
            path = format(path);

            synchronized (lock) {
                if (!paths.contains(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.createFile(path);

                paths.add(path);
                return new FileOutputStream(path.toFile());
            }
        }

        public @NotNull File createDirectories(@NotNull Path path) throws IOException {
            path = format(path);

            synchronized (lock) {
                Files.createDirectories(path);

                paths.add(path);
                return path.toFile();
            }
        }

        public final boolean contains(@NotNull Path path) {
            synchronized (lock) {
                return paths.contains(format(path));
            }
        }

        public @NotNull FileInputStream getInputStream(@NotNull Path path) throws FileNotFoundException {
            path = format(path);

            synchronized (lock) {
                if (!paths.contains(path)) {
                    throw new FileNotFoundException("The path doest not exists in the storage");
                }

                return new FileInputStream(path.toFile());
            }
        }

        public @NotNull FileOutputStream getOutputStream(@NotNull Path path, boolean append) throws FileNotFoundException {
            path = format(path);

            synchronized (lock) {
                if (!paths.contains(path)) {
                    throw new FileNotFoundException("The path doest not exists in the storage: " + path);
                }

                return new FileOutputStream(path.toFile(), append);
            }
        }

        public boolean isDirectory(@NotNull Path path) {
            return contains(path) && Files.isDirectory(path);
        }

        public boolean delete(@NotNull Path path) throws IOException {
            path = format(path);

            synchronized (lock) {
                paths.remove(path);
                return Files.deleteIfExists(path);
            }
        }

        public @NotNull String [] getFolders(@NotNull Path path) throws IOException {
            path = format(path);

            if (!contains(path)) {
                throw new FileNotFoundException("The path doest not exists in the storage: " + path);
            } else synchronized (lock) {
                try (@NotNull DirectoryStream<@NotNull Path> dir = Files.newDirectoryStream(path)) {

                    @NotNull List<@NotNull String> folders = new LinkedList<>();

                    for (@NotNull Path file : dir) {
                        if (Files.isDirectory(file)) {
                            folders.add(file.getFileName().toString());
                        }
                    }

                    return folders.toArray(new String[0]);
                }
            }
        }

        public @NotNull String [] getFiles(@NotNull Path path) throws IOException {
            path = format(path);

            if (!contains(path)) {
                throw new FileNotFoundException("The path doest not exists in the storage: " + path);
            } else synchronized (lock) {
                try (@NotNull DirectoryStream<@NotNull Path> dir = Files.newDirectoryStream(path)) {

                    @NotNull List<@NotNull String> folders = new LinkedList<>();

                    for (@NotNull Path file : dir) {
                        if (!Files.isDirectory(file)) {
                            folders.add(file.getFileName().toString());
                        }
                    }

                    return folders.toArray(new String[0]);
                }
            }
        }

        private @NotNull Path format(@NotNull Path path) {
            return path.startsWith(root.toString()) ? path : root.resolve(path);
        }

        public void deleteAll() {
            synchronized (lock) {
                for (@NotNull Path p : paths) {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException ignore) {
                    }
                }
            }
        }
    }
}
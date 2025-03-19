package dev.hensil.flastro.api.file;

import dev.hensil.flastro.api.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

public interface FileStorage extends Storage {

    static @NotNull Path format(@NotNull String @NotNull [] folders, @NotNull String name) {
        @NotNull StringJoiner pathJoiner = new StringJoiner("\\");

        for (@NotNull String str : folders) {
            pathJoiner.add(str);
        }

        return Paths.get(pathJoiner.toString(), name);
    }

    // Objects

    @NotNull Path getRoot();

    @NotNull FileData getFileData();

    @NotNull FileManager getFileManager();

    // Classes

    interface FileData {

        @NotNull InputStream getInputStream(@NotNull Path path) throws FileNotFoundException;

        @NotNull OutputStream getOutputStream(@NotNull Path path, boolean appends) throws FileNotFoundException;

        @NotNull FileChannel getChannel(@NotNull Path path) throws FileNotFoundException;

        // Defaults

        default @NotNull InputStream getInputStream(@NotNull String @NotNull [] folders, @NotNull String name) throws FileNotFoundException {
            return getInputStream(format(folders, name));
        }

        default @NotNull FileChannel getChannel(@NotNull String @NotNull [] folders, @NotNull String name) throws FileNotFoundException {
            return getChannel(format(folders, name));
        }

        default @NotNull OutputStream getOutputStream(@NotNull String @NotNull [] folders, @NotNull String name, boolean appends) throws FileNotFoundException {
            return getOutputStream(format(folders, name), appends);
        }
    }

    interface FileManager {

        // todo create MetaFile class
        @NotNull File create(@NotNull Path path) throws IOException;

        boolean delete(@NotNull Path path);

        boolean contains(@NotNull Path path);

        // Defaults

        default @NotNull File create(@NotNull String @NotNull [] folders, @NotNull String name) throws IOException {
            return create(format(folders, name));
        }

        default boolean contains(@NotNull String @NotNull [] folders, @NotNull String name) {
            return contains(format(folders, name));
        }

        default boolean delete(@NotNull String @NotNull [] folders, @NotNull String name) {
            return delete(format(folders, name));
        }
    }
}
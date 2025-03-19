package dev.hensil.flastro.api.file;

import dev.hensil.flastro.api.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public interface FileStorage extends Storage {

    @NotNull Path getRoot();

    @NotNull FileManager getFileManager();

    @NotNull FileData getFileData();

    // Classes

    interface FileManager {

        @NotNull MetaFile create(@NotNull Path path) throws IOException;

        @NotNull MetaFile get(@NotNull Path path) throws FileNotFoundException;

        boolean delete(@NotNull Path path);

        boolean contains(@NotNull Path path);

    }

    interface FileData {

        @NotNull InputStream getInputStream(@NotNull Path path) throws FileNotFoundException;

        @NotNull OutputStream getOutputStream(@NotNull Path path, boolean appends) throws FileNotFoundException;

        @NotNull FileChannel getChannel(@NotNull Path path) throws FileNotFoundException;

    }
}

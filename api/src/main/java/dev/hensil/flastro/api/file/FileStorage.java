package dev.hensil.flastro.api.file;

import dev.hensil.flastro.api.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public interface FileStorage extends Storage {

    @NotNull Path getRoot();

    @NotNull Manager getManager();

    // Classes

    interface Manager {

        @NotNull MetaFile create(@NotNull Path path) throws IOException;

        @NotNull MetaFile get(@NotNull Path path) throws FileNotFoundException;

        boolean delete(@NotNull Path path);

        boolean contains(@NotNull Path path);

    }
}

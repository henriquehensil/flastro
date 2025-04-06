package dev.hensil.flastro.core.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

public interface MetaFile {

    @NotNull String getName();

    @NotNull URI getURI();

    @NotNull Instant getCreated();

    @NotNull Optional<@NotNull Instant> getLastWritten();

    @NotNull Optional<@NotNull Instant> getLastRead();

    @NotNull Optional<@NotNull FolderContent> getFolderContent();

    @Range(from = 0, to = Long.MAX_VALUE) long size();

    @Range(from = 0, to = Long.MAX_VALUE) int compressedSize();

    // Classes

    interface FolderContent {

        @Range(from = 0, to = Long.MAX_VALUE) int getAllFiles();

        @Range(from = 0, to = Long.MAX_VALUE) int getAllFolders();

    }
}
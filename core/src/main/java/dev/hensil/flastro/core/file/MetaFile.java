package dev.hensil.flastro.core.file;

import dev.hensil.flastro.core.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;

public interface MetaFile {

    // Objects

    @NotNull General getGeneral();

    @NotNull Periods getPeriods();

    @NotNull Content getContent();

    byte @NotNull [] toBytes();

    // Classes

    interface General {

        @NotNull String getName();

        @NotNull URI getURI();

        boolean isFolder();

    }

    interface Periods {

        @NotNull Time creation();

        @Nullable Time lastWritten();

        @Nullable Time lastRead();

        // Classes

        interface Time {

            @NotNull Instant getInstant();

            @NotNull User getUser();

        }
    }

    interface Content {

        @Range(from = 0, to = Long.MAX_VALUE) long size();

        @Range(from = 0, to = Long.MAX_VALUE) int compressedSize();

        @Nullable Folder getFolderContent();

        // Classes

        interface Folder {

            @Range(from = 0, to = Long.MAX_VALUE) int getAllFiles();

            @Range(from = 0, to = Long.MAX_VALUE) int getAllFolders();

        }
    }
}
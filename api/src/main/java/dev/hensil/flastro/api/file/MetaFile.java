package dev.hensil.flastro.api.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public interface MetaFile {

    @NotNull MetaFile.Data getData();

    @NotNull MetaFile.Time getTime();

    @NotNull MetaFile.Attributes getAttributes();

    // Classes

    interface Data {

        @NotNull Path getPath();

        boolean isDirectory();

        @Range(from = 0, to = Long.MAX_VALUE) long size();

    }

    interface Time {

        @NotNull FileTime getLastModified();

        @NotNull FileTime getLastAccess();

        @NotNull FileTime getCreation();

    }

    interface Attributes {

        @NotNull BasicFileAttributes get();

        boolean isCompressed();

    }
}

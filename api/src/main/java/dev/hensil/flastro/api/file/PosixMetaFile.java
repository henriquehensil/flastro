package dev.hensil.flastro.api.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.PosixFileAttributes;

public interface PosixMetaFile extends MetaFile {

    @NotNull PosixAttributes getAttributes();

    // Classes

    interface PosixAttributes extends Attributes {

        @Override
        @NotNull PosixFileAttributes get();

    }

}

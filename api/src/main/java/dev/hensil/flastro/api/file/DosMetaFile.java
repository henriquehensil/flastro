package dev.hensil.flastro.api.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.DosFileAttributes;

public interface DosMetaFile extends MetaFile {

    @Override
    @NotNull DosAttributes getAttributes();

    // Classes

    interface DosAttributes extends Attributes {

        @Override
        @NotNull DosFileAttributes get();

    }

}

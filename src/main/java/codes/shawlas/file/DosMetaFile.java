package codes.shawlas.file;

import org.jetbrains.annotations.NotNull;

public interface DosMetaFile extends MetaFile {

    @NotNull FatAttributes getFatAttributes();

    interface FatAttributes {

        boolean isArchive();

        boolean isHidden();

        boolean isReadOnly();

        boolean isSystem();

    }
}

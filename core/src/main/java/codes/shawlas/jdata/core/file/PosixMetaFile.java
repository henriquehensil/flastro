package codes.shawlas.jdata.core.file;

import org.jetbrains.annotations.NotNull;

public interface PosixMetaFile extends MetaFile {

    @Override
    @NotNull PosixFilePermissions getPermissions();

    // Classes

    interface PosixFilePermissions extends FilePermissions {

        @NotNull PosixFileOwner getUser();

        @NotNull PosixFileOwner getGroup();

        @NotNull PosixFileOwner getOthers();

        // Classes

        interface PosixFileOwner {

            @NotNull String getName();

            boolean isReadable();

            boolean isWriteable();

            boolean isExecutable();

        }
    }
}

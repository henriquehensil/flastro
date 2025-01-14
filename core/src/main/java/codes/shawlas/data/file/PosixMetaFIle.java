package codes.shawlas.data.file;

import org.jetbrains.annotations.NotNull;

public interface PosixMetaFIle extends MetaFile {

    @Override
    @NotNull PosixFilePermissions getPermissions();

    // Classes

    interface PosixFilePermissions extends FilePermissions {

        @NotNull PosixFileOwner getUser();

        @NotNull PosixFileOwner getGroup();

        @NotNull PosixFileOwner getOthers();

        // Owner

        interface PosixFileOwner {

            @NotNull String getName();

            boolean isReadable();

            boolean isWriteable();

            boolean isExecutable();

        }

        @Override
        default boolean isFullyReadable() {
            return getUser().isReadable() && getGroup().isReadable() && getOthers().isReadable();
        }

        @Override
        default boolean isFullyWritable() {
            return getUser().isWriteable() && getGroup().isWriteable() && getOthers().isWriteable();
        }

        @Override
        default boolean isFullyExecutable() {
            return getUser().isExecutable() && getGroup().isExecutable() && getOthers().isExecutable();
        }
    }
}

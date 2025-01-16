package codes.shawlas.data.impl.file;

import codes.shawlas.data.file.PosixMetaFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;

final class SimplePosixMetaFile extends AbstractMetaFile implements PosixMetaFile {

    private final @NotNull PosixFileAttributes attributes;
    private final @NotNull PosixFilePermissions permissions = new Permissions();

    private SimplePosixMetaFile(@NotNull File file) throws IOException {
        super(file, PosixFileAttributes.class);
        this.attributes = (PosixFileAttributes) super.attributes;
    }

    @Override
    public @NotNull PosixFilePermissions getPermissions() {
       return permissions;
    }

    // Classes

    private final class Permissions implements PosixFilePermissions {

        private final @NotNull UserOwner user = new UserOwner();
        private final @NotNull GroupOwner group = new GroupOwner();
        private final @NotNull OthersOwner others = new OthersOwner();

        @Override
        public @NotNull PosixFileOwner getUser() {
            return user;
        }

        @Override
        public @NotNull PosixFileOwner getGroup() {
            return group;
        }

        @Override
        public @NotNull PosixFileOwner getOthers() {
            return others;
        }

        // Classes

        private final class UserOwner implements PosixFileOwner {
            @Override
            public @NotNull String getName() {
                return attributes.owner().getName();
            }

            @Override
            public boolean isReadable() {
                return attributes.permissions().contains(PosixFilePermission.OWNER_READ);
            }

            @Override
            public boolean isWriteable() {
                return attributes.permissions().contains(PosixFilePermission.OWNER_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return attributes.permissions().contains(PosixFilePermission.OWNER_EXECUTE);
            }
        }
        private final class GroupOwner implements PosixFileOwner {
            @Override
            public @NotNull String getName() {
                return attributes.group().getName();
            }

            @Override
            public boolean isReadable() {
                return attributes.permissions().contains(PosixFilePermission.GROUP_READ);
            }

            @Override
            public boolean isWriteable() {
                return attributes.permissions().contains(PosixFilePermission.GROUP_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return attributes.permissions().contains(PosixFilePermission.GROUP_EXECUTE);
            }
        }
        private final class OthersOwner implements PosixFileOwner {
            @Override
            public @NotNull String getName() {
                return "Others";
            }

            @Override
            public boolean isReadable() {
                return attributes.permissions().contains(PosixFilePermission.OTHERS_READ);
            }

            @Override
            public boolean isWriteable() {
                return attributes.permissions().contains(PosixFilePermission.OTHERS_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return attributes.permissions().contains(PosixFilePermission.OTHERS_EXECUTE);
            }
        }
    }
}
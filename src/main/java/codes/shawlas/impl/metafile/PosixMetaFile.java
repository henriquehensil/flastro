package codes.shawlas.impl.metafile;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public final class PosixMetaFile extends MetaFileImpl {

    private final @NotNull PosixFileAttributes posAttr;
    private final @NotNull PosixMetaFilePermissions posPermissions = new PosixMetaFilePermissions();

    public PosixMetaFile(@NotNull File file) throws IOException {
        super(file, PosixFileAttributes.class);
        this.posAttr = (PosixFileAttributes) basicAttr;
    }

    @Override
    public @NotNull JsonObject serialize() {
        @NotNull JsonObject object = super.serialize().getAsJsonObject();
        // replace permissions
        object.add("permissions", this.getPosixPermissions().serialize());

        @NotNull JsonObject owners = new JsonObject();
        owners.addProperty("user", this.getPosixPermissions().getUser().getName());
        owners.addProperty("group", this.getPosixPermissions().getGroup().getName());

        //added owner
        object.add("owner", owners);

        return object;
    }

    public @NotNull PosixMetaFilePermissions getPosixPermissions() {
        return posPermissions;
    }

    public final class PosixMetaFilePermissions implements FilePermissions {

        private final @NotNull Set<@NotNull PosixFilePermission> permissions = posAttr.permissions();

        private final @NotNull GroupOwner group = new GroupOwner();
        private final @NotNull UserOwner user = new UserOwner();
        private final @NotNull OthersOwner others = new OthersOwner();

        private @NotNull JsonObject serialize() {
            @NotNull JsonObject object = new JsonObject();

            object.addProperty("fullReadable", isFullyReadable());
            object.addProperty("fullWritable", isFullyWritable());
            object.addProperty("fullExecutable", isFullyExecutable());

            return object;
        }

        public @NotNull UserOwner getUser() {
            return user;
        }

        public @NotNull GroupOwner getGroup() {
            return group;
        }

        public @NotNull OthersOwner getOthers() {
            return others;
        }

        @Override
        public boolean isFullyReadable() {
            return user.isReadable() && group.isReadable() && others.isReadable();
        }

        @Override
        public boolean isFullyWritable() {
            return user.isWritable() && group.isWritable() && others.isWritable();
        }

        @Override
        public boolean isFullyExecutable() {
            return user.isExecutable() && group.isExecutable() && others.isExecutable();
        }

        // Classes

        public final class UserOwner implements PermissionsViews {

            private UserOwner() {
            }

            private @NotNull JsonObject serialize() {
                @NotNull JsonObject object = new JsonObject();

                object.addProperty("readable", this.isReadable());
                object.addProperty("writable", this.isWritable());
                object.addProperty("executable", this.isExecutable());

                return object;
            }

            public @NotNull String getName() {
                return posAttr.owner().getName();
            }

            @Override
            public boolean isReadable() {
                return permissions.contains(PosixFilePermission.OWNER_READ);
            }

            @Override
            public boolean isWritable() {
                return permissions.contains(PosixFilePermission.OWNER_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return permissions.contains(PosixFilePermission.OWNER_EXECUTE);
            }
        }

        public final class GroupOwner implements PermissionsViews {

            private GroupOwner() {
            }

            private @NotNull JsonObject serialize() {
                @NotNull JsonObject object = new JsonObject();

                object.addProperty("readable", this.isReadable());
                object.addProperty("writable", this.isWritable());
                object.addProperty("executable", this.isExecutable());

                return object;
            }

            public @NotNull String getName() {
                return posAttr.group().getName();
            }

            @Override
            public boolean isReadable() {
                return permissions.contains(PosixFilePermission.GROUP_READ);
            }

            @Override
            public boolean isWritable() {
                return permissions.contains(PosixFilePermission.GROUP_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return permissions.contains(PosixFilePermission.GROUP_EXECUTE);
            }
        }

        public final class OthersOwner implements PermissionsViews {

            private @NotNull JsonObject serialize() {
                @NotNull JsonObject object = new JsonObject();

                object.addProperty("readable", this.isReadable());
                object.addProperty("writable", this.isWritable());
                object.addProperty("executable", this.isExecutable());

                return object;
            }

            @Override
            public boolean isReadable() {
                return permissions.contains(PosixFilePermission.OTHERS_READ);
            }

            @Override
            public boolean isWritable() {
                return permissions.contains(PosixFilePermission.OTHERS_WRITE);
            }

            @Override
            public boolean isExecutable() {
                return permissions.contains(PosixFilePermission.OTHERS_EXECUTE);
            }
        }

        private interface PermissionsViews {
            boolean isReadable();
            boolean isWritable();
            boolean isExecutable();
        }
    }
}

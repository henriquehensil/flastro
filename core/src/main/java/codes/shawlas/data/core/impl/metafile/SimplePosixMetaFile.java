package codes.shawlas.data.core.impl.metafile;

import codes.shawlas.data.core.file.PosixMetaFile;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;

public final class SimplePosixMetaFile extends AbstractMetaFile implements PosixMetaFile {

    private final @NotNull PosixFilePermissionsImpl permissions = new PosixFilePermissionsImpl();
    private final @NotNull PosixFileAttributes attributes;

    public SimplePosixMetaFile(@NotNull File file) throws IOException {
        super(file, PosixFileAttributes.class);
        this.attributes = (PosixFileAttributes) super.attributes;
    }

    @Override
    public @NotNull JsonObject serialize() {
        @NotNull JsonObject owners = new JsonObject();
        owners.addProperty("user", getPermissions().getUser().getName());
        owners.addProperty("group", getPermissions().getGroup().getName());

        @NotNull JsonObject object = SimpleMetaFile.jsonProvider(this);
        object.add("owners", owners);

        // replaced
        object.add("permissions", permissions.serialize());

        return object;
    }

    @Override
    public @NotNull PosixFilePermissions getPermissions() {
        return permissions;
    }

    private final class PosixFilePermissionsImpl implements PosixFilePermissions {

        private final @NotNull PosixFileOwnerProviders.PosixFileUser user = new PosixFileOwnerProviders().user;
        private final @NotNull PosixFileOwnerProviders.PosixFileGroup group = new PosixFileOwnerProviders().group;
        private final @NotNull PosixFileOwnerProviders.PosixFileOthers others = new PosixFileOwnerProviders().others;

        public @NotNull JsonObject serialize() {
            @NotNull JsonObject user = new JsonObject();
            user.addProperty("readable", getUser().isReadable());
            user.addProperty("writable", getUser().isWriteable());
            user.addProperty("executable", getUser().isExecutable());

            @NotNull JsonObject group = new JsonObject();
            user.addProperty("readable", getGroup().isReadable());
            user.addProperty("writable", getGroup().isWriteable());
            user.addProperty("executable", getGroup().isExecutable());

            @NotNull JsonObject others = new JsonObject();
            user.addProperty("readable", getOthers().isReadable());
            user.addProperty("writable", getOthers().isWriteable());
            user.addProperty("executable", getOthers().isExecutable());

            @NotNull JsonObject object = new JsonObject();
            object.add("user", user);
            object.add("group", group);
            object.add("others", others);

            return object;
        }

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

        @Override
        public boolean isFullyReadable() {
            return getUser().isReadable() && getGroup().isReadable() && getOthers().isReadable();
        }

        @Override
        public boolean isFullyWritable() {
            return getUser().isWriteable() && getGroup().isWriteable() && getOthers().isWriteable();
        }

        @Override
        public boolean isFullyExecutable() {
            return getUser().isExecutable() && getGroup().isExecutable() && getOthers().isExecutable();
        }

        /*
         *
         * Providers
         *
         * */
        private final class PosixFileOwnerProviders {
            private final @NotNull PosixFileUser user = new PosixFileUser();
            private final @NotNull PosixFileGroup group = new PosixFileGroup();
            private final @NotNull PosixFileOthers others = new PosixFileOthers();

            // Classes
            private final class PosixFileUser implements PosixFilePermissions.PosixFileOwner {
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
                    return attributes.permissions().contains(PosixFilePermission.OTHERS_EXECUTE);
                }
            }
            private final class PosixFileGroup implements PosixFilePermissions.PosixFileOwner {
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
            private final class PosixFileOthers implements PosixFilePermissions.PosixFileOwner {
                @Override
                public @NotNull String getName() {
                    return "others";
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
}

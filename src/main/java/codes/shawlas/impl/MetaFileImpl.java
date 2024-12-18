package codes.shawlas.impl;

import codes.shawlas.file.MetaFile;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;

public class MetaFileImpl implements MetaFile {

    private final @NotNull File file;
    private final @NotNull BasicFileAttributes basicAttr;

    private final @NotNull FileDataImpl data = new FileDataImpl();
    private final @NotNull FileTimesImpl times = new FileTimesImpl();
    private final @NotNull FilePermissionsImpl permissions = new FilePermissionsImpl();

    public MetaFileImpl(@NotNull File file) throws IOException {
        this.file = file;
        this.basicAttr = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class).readAttributes();
    }

    public @NotNull JsonObject serialize() {
        @NotNull JsonObject object = new JsonObject();

        object.addProperty("name", getName());
        object.addProperty("type", getType());
        object.addProperty("key", getKey());
        object.addProperty("path", getPath().toString());

        object.add("data", this.data.serialize());
        object.add("times", this.times.serialize());
        object.add("permissions", this.permissions.serialize());

        return object;
    }

    // Getters

    public final @NotNull String getName() {
        return getPath().getFileName().toString();
    }

    public final @Nullable String getType() {
        int index = getName().lastIndexOf(".");
        return index > 0 ? getName().substring(index) : null;
    }

    @Override
    public final @Nullable String getKey() {
        @Nullable Object key = basicAttr.fileKey();
        return key != null ? key.toString() : null;
    }

    @Override
    public final @NotNull FileData getData() {
        return data;
    }

    @Override
    public final @NotNull FileTimes getTimes() {
        return times;
    }

    @Override
    public final @NotNull FilePermissions getPermissions() {
        return permissions;
    }

    @Override
    public final @NotNull Path getPath() {
        return file.toPath();
    }

    // Classes

    private final class FileDataImpl implements FileData {

        private FileDataImpl() {
        }

        private @NotNull JsonObject serialize() {
            @NotNull JsonObject object = new JsonObject();
            object.addProperty("size", size());

            return object;
        }

        @Override
        public @NotNull InputStream getInputStream() throws IOException {
            return Files.newInputStream(file.toPath());
        }

        @Override
        @Blocking
        public void write(@NotNull OutputStream outputStream) throws IOException {
            try (@NotNull InputStream in = getInputStream()) {
                byte @NotNull [] buffer = new byte[8192];

                int read;

                while ((read = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                    outputStream.flush();
                }
            }
        }

        @Override
        public long size() {
            return basicAttr.size();
        }
    }

    private final class FileTimesImpl implements FileTimes {

        private FileTimesImpl() {
        }

        private @NotNull JsonObject serialize() {
            @NotNull JsonObject object = new JsonObject();
            object.addProperty("created", getCreated().toString());
            object.addProperty("modified", getModified().toString());
            object.addProperty("access", getAccess().toString());

            return object;
        }

        @Override
        public @NotNull FileTime getCreated() {
            return basicAttr.creationTime();
        }

        @Override
        public @NotNull FileTime getModified() {
            return basicAttr.lastModifiedTime();
        }

        @Override
        public @NotNull FileTime getAccess() {
            return basicAttr.lastAccessTime();
        }
    }

    private final class FilePermissionsImpl implements FilePermissions {

        private FilePermissionsImpl() {
        }

        private @NotNull JsonObject serialize() {
            @NotNull JsonObject object = new JsonObject();
            object.addProperty("readable", isFullyReadable());
            object.addProperty("writable", isFullyWritable());
            object.addProperty("executable", isFullyExecutable());

            return object;
        }

        /**
         * @throws SecurityException if the file security manager denies access
         * */
        @Override
        public boolean isFullyReadable() throws SecurityException {
            return file.canRead();
        }

        /**
         * @throws SecurityException if the file security manager denies access
         * */
        @Override
        public boolean isFullyWritable() throws SecurityException {
            return file.canWrite();
        }

        /**
         * @throws SecurityException if the file security manager denies access
         * */
        @Override
        public boolean isFullyExecutable() throws SecurityException {
            return file.canExecute();
        }
    }
}
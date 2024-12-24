package codes.shawlas.impl.metafile;

import codes.shawlas.exception.file.InvalidFileAttributesException;
import codes.shawlas.file.MetaFile;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public abstract class AbstractMetaFile implements MetaFile {

    // Static initializers

    static @NotNull JsonObject jsonProvider(@NotNull MetaFile metaFile) {
        @NotNull JsonObject general = new JsonObject();
        general.addProperty("name", metaFile.getPath().getFileName().toString());
        general.addProperty("key", metaFile.getKey());
        general.addProperty("size", metaFile.getData().size());
        general.addProperty("path", metaFile.getPath().toString());

        @NotNull JsonObject times = new JsonObject();
        times.addProperty("created", metaFile.getTimes().getCreated().toString());
        times.addProperty("modified", metaFile.getTimes().getModified().toString());
        times.addProperty("access", metaFile.getTimes().getAccess().toString());

        @NotNull JsonObject permissions = new JsonObject();
        permissions.addProperty("readable", metaFile.getPermissions().isFullyReadable());
        permissions.addProperty("writable", metaFile.getPermissions().isFullyWritable());
        permissions.addProperty("executable", metaFile.getPermissions().isFullyExecutable());

        @NotNull JsonObject object = new JsonObject();
        object.add("general", general);
        object.add("times", times);
        object.add("permissions", permissions);

        return object;
    }

    // Objects

    protected final @NotNull BasicFileAttributes attributes;

    private final @NotNull File file;
    private final @NotNull FileData data = new FileDataImpl();
    private final @NotNull FileTimes times = new FileTimesImpl();
    private final @NotNull FilePermissions permissions = new FilePermissionsImpl();

    /**
     * @throws IOException if an I/O errors occurs
     * @throws InvalidFileAttributesException  if an attributes of the given type are not supported
     * @throws SecurityException if the security manager of this file prevents any operation
     * */
    protected <T extends BasicFileAttributes> AbstractMetaFile(@NotNull File file, @NotNull Class<T> attribute) throws IOException, InvalidFileAttributesException, SecurityException {
        try {
            this.attributes = Files.readAttributes(file.toPath(), attribute);
            this.file = file;
        } catch (UnsupportedOperationException e) {
            throw new InvalidFileAttributesException(e.getMessage());
        }
    }

    public abstract @NotNull JsonObject serialize();

    // Getters

    @Override
    public final @Nullable String getKey() {
        return attributes.fileKey() != null ? attributes.fileKey().toString() : null;
    }

    @Override
    public @NotNull FileData getData() {
        return data;
    }

    @Override
    public @NotNull FileTimes getTimes() {
        return times;
    }

    @Override
    public @NotNull FilePermissions getPermissions() {
        return permissions;
    }

    @Override
    public final @NotNull Path getPath() {
        return file.toPath();
    }

    // Classes
    private final class FileDataImpl implements FileData {

        @Override
        public @NotNull InputStream getInputStream() throws IOException {
            return Files.newInputStream(getPath());
        }

        @Override
        @Blocking
        public void write(@NotNull OutputStream outputStream) throws IOException {
            try (@NotNull InputStream input = getInputStream()) {
                byte @NotNull [] bytes = new byte[8192]; //8KB

                int read;
                while ((read = input.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                    outputStream.flush();
                }
            }
        }

        @Override
        public long size() {
            return attributes.size();
        }
    }

    private final class FileTimesImpl implements FileTimes {

        @Override
        public @NotNull FileTime getCreated() {
            return attributes.creationTime();
        }

        @Override
        public @NotNull FileTime getModified() {
            return attributes.lastModifiedTime();
        }

        @Override
        public @NotNull FileTime getAccess() {
            return attributes.lastAccessTime();
        }
    }

    private final class FilePermissionsImpl implements FilePermissions {

        @Override
        public boolean isFullyReadable() {
            return file.canRead();
        }

        @Override
        public boolean isFullyWritable() {
            return file.canWrite();
        }

        @Override
        public boolean isFullyExecutable() {
            return file.canExecute();
        }
    }

    // Native

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull SimpleDosMetaFile that = (SimpleDosMetaFile) o;
        return this.serialize().equals(that.serialize());
    }

    @Override
    public int hashCode() {
        return serialize().hashCode();
    }
}

package codes.shawlas.data.impl.file;

import codes.shawlas.data.file.MetaFile;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

abstract class AbstractMetaFile implements MetaFile {

    // Objects

    protected final @NotNull BasicFileAttributes attributes;

    private final @NotNull File file;
    private final @NotNull FileData data = new FileDataImpl();
    private final @NotNull FileTimes times = new FileTimesImpl();
    private final @NotNull FilePermissions permissions = new FilePermissionsImpl();

    /**
     * @throws IOException if an I/O errors occurs
     * */
    protected <T extends BasicFileAttributes> AbstractMetaFile(@NotNull File file, @NotNull Class<T> attribute) throws IOException {
        this.attributes = Files.readAttributes(file.toPath(), attribute);
        this.file = file;
    }

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
    public @NotNull File getFile() {
        return file;
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
}
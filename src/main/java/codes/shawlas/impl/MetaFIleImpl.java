package codes.shawlas.impl;

import codes.shawlas.file.MetaFile;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public final class MetaFIleImpl implements MetaFile {

    private final @NotNull File file;
    private final @NotNull BasicFileAttributeView attributeView;
    private final @NotNull BasicFileAttributes attributes;

    private final @NotNull FileKey key = new FileKeyImpl();
    private final @NotNull FileData data = new FileDataImpl();
    private final @NotNull FileTimes times = new FileTimesImpl();

    public MetaFIleImpl(@NotNull File file) throws IOException {
        this.file = file;
        this.attributeView = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        this.attributes = attributeView.readAttributes();
    }

    public MetaFIleImpl(@NotNull Path path) throws IOException {
        this.file = path.toFile();
        this.attributeView = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        this.attributes = attributeView.readAttributes();
    }

    @Override
    public @NotNull FileKey getKey() {
        return key;
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
        return null;
    }

    @Override
    public @NotNull FileOwners getOwners() {
        return null;
    }

    @Override
    public @NotNull Path getPath() {
        return file.toPath();
    }

    // Classes

    private final class FileKeyImpl implements FileKey {

        private final @Nullable Object key = attributes.fileKey();

        private FileKeyImpl() throws IOException {
        }

        @Override
        public @NotNull System getSystem() {
            @Nullable String key = this.key == null ? null : this.key.toString();

            if (key == null) {
                return System.UNSUPPORTED;
            } else if (key.contains("dev=") && key.contains("ino=")) {
                return System.UNIX;
            } else if (key.contains("fileIndex=")) {
                return System.WINDOWS;
            } else {
                return System.OTHERS;
            }
        }

        @Override
        public @Nullable String getKey() {
            return key != null ? key.toString() : null;
        }

        @Override
        public @NotNull String toString() {
            return key != null ? key.toString() : super.toString();
        }
    }

    private final class FileDataImpl implements FileData {

        private FileDataImpl() {
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
            return attributes.size();
        }
    }

    private final class FileTimesImpl implements FileTimes {

        private FileTimesImpl() {
        }

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
}

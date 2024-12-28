package codes.shawlas.data.core.file;

import codes.shawlas.data.core.impl.metafile.SimpleDosMetaFile;
import codes.shawlas.data.core.impl.metafile.SimpleMetaFile;
import codes.shawlas.data.core.impl.metafile.SimplePosixMetaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;

public interface MetaFile {

    static @NotNull MetaFile create(@NotNull File file) throws IOException {
        @NotNull Path path = file.toPath();

        if (Files.getFileAttributeView(path, DosFileAttributeView.class) != null) {
            return new SimpleDosMetaFile(file);
        } else if (Files.getFileAttributeView(path, PosixFileAttributeView.class) != null) {
            return new SimplePosixMetaFile(file);
        } else {
            return new SimpleMetaFile(file);
        }
    }

    /**
     * @return The Key represented in String or {@code null} if key is not founded
     * */
    @Nullable String getKey();

    @NotNull FileData getData();

    @NotNull FileTimes getTimes();

    @NotNull FilePermissions getPermissions();

    @NotNull Path getPath();

    // Classes

    interface FileData {

        /**
         * @throws IOException if an I/O error occurs when getting InputStream from the file
         * */
        @NotNull InputStream getInputStream() throws IOException;

        /**
         * @throws IOException if an I/O error occurs while written or if {@code getInputStream} error occurs
         * */
        void write(@NotNull OutputStream outputStream) throws IOException;

        long size();

    }

    interface FileTimes {

        @NotNull FileTime getCreated();

        @NotNull FileTime getModified();

        @NotNull FileTime getAccess();

    }

    interface FilePermissions {

        boolean isFullyReadable();

        boolean isFullyWritable();

        boolean isFullyExecutable();

    }
}
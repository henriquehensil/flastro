package codes.shawlas.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public interface MetaFile {

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
package ghostface.dev.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public interface MetaFile {

    @NotNull FileKey getKey();

    @NotNull FileData getData();

    @NotNull FileTimes getTimes();

    @NotNull Path getPath();

    @NotNull String getUser();

    // Classes

    interface FileKey extends CharSequence {

        @NotNull System getSystem();

        @Nullable String getString();

        enum System {
            UNIX,
            WINDOWS,
            OTHERS,
            UNSUPPORTED;
        }
    }

    interface FileData {

        @NotNull InputStream getInputStream() throws IOException;

        void write(@NotNull OutputStream outputStream) throws IOException;

        byte @NotNull [] getBytes();

        long size();

    }

    interface FileTimes {

        @NotNull FileTime getCreated();

        @NotNull FileTime getModified();

        @NotNull FileTime getAccess();

    }
}

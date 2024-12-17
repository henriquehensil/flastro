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

    @NotNull FilePermissions getPermissions();

    @NotNull FileOwners getOwners();

    @NotNull Path getPath();

    // Classes

    interface FileKey {

        @NotNull System getSystem();

        @Nullable String getKey();

        @Override
        @NotNull String toString();

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

        long size();

    }

    interface FileTimes {

        @NotNull FileTime getCreated();

        @NotNull FileTime getModified();

        @NotNull FileTime getAccess();

    }

    interface FilePermissions {

        boolean isReadable(@NotNull FileOwner owner);

        boolean isWritable(@NotNull FileOwner owner);

        boolean isExecutable(@NotNull FileOwner owner);

        // Todo: setPermissions
    }

    interface FileOwners {

        @NotNull FileUserOwner getUser();

        @NotNull FileGroupOwner getGroup();

    }
}
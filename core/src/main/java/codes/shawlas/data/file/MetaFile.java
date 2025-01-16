package codes.shawlas.data.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;

public interface MetaFile {

    // Static initializers

    static @NotNull MetaFile create(@NotNull File file) throws IOException {
        if (Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class) != null) {
            return PosixMetaFile.create(file);
        } else if (Files.getFileAttributeView(file.toPath(), DosFileAttributeView.class) != null) {
            return DosMetaFile.create(file);
        } else if (Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class) != null) {
            try {
                //noinspection unchecked
                @NotNull Class<? extends @NotNull MetaFile> reference = (Class<? extends MetaFile>) Class.forName("codes.shawlas.data.impl.file.AbstractMetaFile");
                @NotNull Constructor<? extends @NotNull MetaFile> constructor = reference.getDeclaredConstructor(File.class);

                return constructor.newInstance(file);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Cannot find the Dos File implementation", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot find the Dos file Implementation constructor", e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot instantiate the implementation class");
            }
        }

        throw new UnsupportedOperationException("Cannot find the attribute file");
    }

    // Objects

    /**
     * @return The Key represented in String or {@code null} if key is not founded
     * */
    @Nullable String getKey();

    @NotNull FileData getData();

    @NotNull FileTimes getTimes();

    @NotNull FilePermissions getPermissions();

    @NotNull File getFile();

    default @NotNull Path getPath() {
        return getFile().toPath();
    }

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

package codes.shawlas.data.file;

import codes.shawlas.data.exception.file.UnsupportedAttributeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface PosixMetaFile extends MetaFile {

    // Static initializers

    static @NotNull PosixMetaFile create(@NotNull File file) throws IOException {
        try {
            // noinspection unchecked
            @NotNull Class<? extends @NotNull PosixMetaFile> reference = (Class<? extends PosixMetaFile>) Class.forName("codes.shawlas.data.impl.file.SimplePosixMetaFile");
            @NotNull Constructor<? extends @NotNull PosixMetaFile> constructor = reference.getDeclaredConstructor(File.class);

            return constructor.newInstance(file);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedAttributeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the Dos File implementation", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the Dos file Implementation constructor", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementation class");
        }
    }

    // Objects

    @Override
    @NotNull PosixFilePermissions getPermissions();

    // Classes

    interface PosixFilePermissions extends FilePermissions {

        @NotNull PosixFileOwner getUser();

        @NotNull PosixFileOwner getGroup();

        @NotNull PosixFileOwner getOthers();

        // Owner

        interface PosixFileOwner {

            @NotNull String getName();

            boolean isReadable();

            boolean isWriteable();

            boolean isExecutable();

        }

        @Override
        default boolean isFullyReadable() {
            return getUser().isReadable() && getGroup().isReadable() && getOthers().isReadable();
        }

        @Override
        default boolean isFullyWritable() {
            return getUser().isWriteable() && getGroup().isWriteable() && getOthers().isWriteable();
        }

        @Override
        default boolean isFullyExecutable() {
            return getUser().isExecutable() && getGroup().isExecutable() && getOthers().isExecutable();
        }
    }
}
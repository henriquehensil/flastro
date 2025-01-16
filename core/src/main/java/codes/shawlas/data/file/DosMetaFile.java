package codes.shawlas.data.file;

import codes.shawlas.data.exception.file.UnsupportedAttributeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface DosMetaFile extends MetaFile {

    // Static initializers

    static @NotNull DosMetaFile create(@NotNull File file) throws IOException {
        try {
            // noinspection unchecked
            @NotNull Class<? extends @NotNull DosMetaFile> reference = (Class<? extends DosMetaFile>) Class.forName("codes.shawlas.data.impl.file.SimpleDosMetaFile");
            @NotNull Constructor<? extends @NotNull DosMetaFile> constructor = reference.getDeclaredConstructor(File.class);

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

    @NotNull FatAttributes getFatAttributes();

    interface FatAttributes {

        boolean isArchive();

        boolean isHidden();

        boolean isReadOnly();

        boolean isSystem();

    }
}

package ghostface.dev.storage.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public interface FileStorage {

    @NotNull Path getDefault();

    @NotNull Optional<File> get(@NotNull String name);

    @NotNull Optional<File> get(@NotNull Path name);

    boolean save(@NotNull String name, @NotNull InputStream stream) throws IOException;

    boolean delete(@NotNull String name);

    boolean delete(@NotNull Path path);

    boolean deleteAll();

}

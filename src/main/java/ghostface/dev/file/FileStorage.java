package ghostface.dev.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public interface FileStorage {

    @NotNull Path getDefault();

    @NotNull Optional<File> get(@NotNull String name);

    @NotNull Optional<File> get(@NotNull Path path);

    boolean delete(@NotNull String name);

    boolean delete(@NotNull Path path);

}

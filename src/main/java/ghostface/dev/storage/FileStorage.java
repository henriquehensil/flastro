package ghostface.dev.storage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public interface FileStorage {

    @NotNull Optional<File> get(@NotNull Path path);

}

package ghostface.dev.storage.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface FileStorage {

    @NotNull Optional<File> get(@NotNull Path path);

    @NotNull CompletableFuture<Boolean> add(@NotNull File file);

    @NotNull CompletableFuture<Boolean> remove(@NotNull File file);

    @NotNull CompletableFuture<Boolean> remove(@NotNull Path path);

}

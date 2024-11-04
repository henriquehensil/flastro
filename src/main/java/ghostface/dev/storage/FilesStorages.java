package ghostface.dev.storage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FilesStorages {

    @NotNull CompletableFuture<@NotNull Boolean> save(@NotNull File file);

    @NotNull CompletableFuture<@NotNull Boolean> delete(@NotNull File file);

    @NotNull CompletableFuture<@NotNull Boolean> delete(@NotNull Path path);

}

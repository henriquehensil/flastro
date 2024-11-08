package ghostface.dev.storage.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface FileStorage {

    @NotNull Path getDefault();

    @NotNull Optional<File> get(@NotNull String name);

    @NotNull Optional<File> get(@NotNull Path name);

    @NotNull CompletableFuture<Boolean> save(@NotNull String name, @NotNull InputStream stream);

    @NotNull CompletableFuture<Boolean> delete(@NotNull String name);

    @NotNull CompletableFuture<Boolean> delete(@NotNull Path path);

    @NotNull CompletableFuture<Boolean> deleteAll();

}

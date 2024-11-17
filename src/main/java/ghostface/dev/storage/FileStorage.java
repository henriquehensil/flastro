package ghostface.dev.storage;

import ghostface.dev.content.KeyContent;
import ghostface.dev.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface FileStorage extends KeyContent<String, File> {

    @NotNull Database database();

    @NotNull Optional<File> get(@NotNull Path path);

    @NotNull Path getDefault();

    @NotNull CompletableFuture<Boolean> create(@NotNull String name);

    @NotNull CompletableFuture<Boolean> create(@NotNull String folder, @NotNull String name);

    // Implementations

    @Override
    @NotNull Optional<File> get(@NotNull String id);

    @Override
    boolean put(@NotNull String id, @NotNull File file);

    @Override
    boolean delete(@NotNull String id);

    @Override
    @Unmodifiable @NotNull Collection<File> toCollection();
}

package ghostface.dev.impl.file;

import ghostface.dev.storage.file.FileStorage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

public final class FileStorageImpl implements FileStorage {

    // Static initializers

    private static final @NotNull FileStorage instance;

    static {
        instance = new FileStorageImpl();
    }

    public static @NotNull FileStorage getInstance() {
        return instance;
    }

    // Objects

    private final @NotNull Map<@NotNull String, @NotNull File> files = new ConcurrentHashMap<>();

    private FileStorageImpl() {}

    @Override
    public @NotNull Path getDefault() {
        return Paths.get(System.getProperty("user.dir") + "\\storage");
    }

    @Override
    public @NotNull Optional<File> get(@NotNull String name) {
        return Optional.ofNullable(files.get(name));
    }

    @Override
    public @NotNull Optional<File> get(@NotNull Path path) {
        return Optional.ofNullable(files.get(path.getFileName().toString()));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> save(@NotNull String name, @NotNull InputStream stream) {
        return CompletableFuture.supplyAsync(() -> {
            if (files.containsKey(name)) {
                return false;
            }

            try {
                if (name.contains("\\")) {
                    throw new UnsupportedOperationException("name cannot be a URI");
                }

                @NotNull File path = new File(getDefault().resolve(name).toString());
                path.mkdirs();

                @NotNull File file = new File(path, name);

                try (@NotNull FileOutputStream output = new FileOutputStream(file)) {
                    byte @NotNull [] bytes = new byte[8192];

                    int read;
                    while ((read = stream.read(bytes)) != -1) {
                        output.write(bytes, 0, read);
                        output.flush();
                    }

                    files.put(name, file);
                    return true;
                }
            } catch (Throwable e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> delete(@NotNull String name) {
        @NotNull CompletableFuture<Boolean> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                future.complete(Files.deleteIfExists(getDefault().resolve(name)));
            } catch (Throwable e) {
               future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> delete(@NotNull Path path) {
        @NotNull CompletableFuture<Boolean> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                if (path.equals(getDefault())) {
                    throw new IllegalArgumentException("Cannot delete this file path");
                } else {
                    future.complete(Files.deleteIfExists(path));
                }
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAll() {
        @NotNull CompletableFuture<Boolean> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            if (files.isEmpty()) {
                future.complete(false);
            } else try {
                files.values().forEach(file -> {
                    file.getParentFile().deleteOnExit();
                    file.getAbsoluteFile().deleteOnExit();
                });
                files.clear();
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }

            future.complete(true);
        });

        return future;
    }
}

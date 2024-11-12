package ghostface.dev.impl;

import com.google.gson.JsonElement;
import ghostface.dev.file.FileStorage;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class JsonFileStorage implements FileStorage {

    private final @NotNull Map<@NotNull Path, @NotNull File> fileMap = new ConcurrentHashMap<>();
    private final @NotNull Path defaultPath;

    public JsonFileStorage(@NotNull Path defaultPath) {
        @NotNull File file = defaultPath.toFile();

        if (!file.isDirectory() && !file.mkdirs()) {
            throw new RuntimeException("Cannot create directory: " + file.getAbsolutePath());
        }

        this.defaultPath = defaultPath;
    }

    @Override
    public @NotNull Path getDefault() {
        return defaultPath;
    }

    @Override
    public @NotNull Optional<File> get(@NotNull String name) {
        return Optional.ofNullable(fileMap.get(Paths.get(name)));
    }

    @Override
    public @NotNull Optional<File> get(@NotNull Path path) {
        return Optional.ofNullable(fileMap.get(path));
    }

    @Blocking
    public @NotNull CompletableFuture<Boolean> save(@NotNull String name, @NotNull JsonElement json) {
        @NotNull CompletableFuture<Boolean> future = new CompletableFuture<>();

        @NotNull Path path = Paths.get(name.replace(" ", "-") + (name.endsWith(".json") ? name : name.concat(".json")));
        @NotNull File file = path.toFile();

        CompletableFuture.runAsync(() -> {
            try {
                if (fileMap.containsKey(path)) {
                    future.complete(false);
                } else if (!file.isFile() && !file.createNewFile()) {
                    throw new RuntimeException("Cannot create file: " + file.getAbsolutePath());
                } else try (@NotNull FileOutputStream output = new FileOutputStream(file)) {
                    output.write(json.toString().getBytes(StandardCharsets.UTF_8));
                    output.flush();
                    future.complete(true);
                }
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public boolean delete(@NotNull String name) {
        return fileMap.remove(defaultPath.resolve(name)) != null;
    }

    @Override
    public boolean delete(@NotNull Path path) {
        return fileMap.remove(path) != null;
    }
}

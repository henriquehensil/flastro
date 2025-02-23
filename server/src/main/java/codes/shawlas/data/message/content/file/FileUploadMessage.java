package codes.shawlas.data.message.content.file;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FileUploadMessage implements Message.Input {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull Path path;
    private final @NotNull String name;
    private final int size;

    public FileUploadMessage(
            @NotNull UUID id,
            @NotNull OffsetDateTime time,
            @NotNull Path path,
            @NotNull String name,
            int size
    ) {
        if (size < 0) {
            throw new IllegalArgumentException("Illegal size value");
        }

        this.id = id;
        this.time = time;
        this.path = path;
        this.name = name;
        this.size = size;
    }

    @Override
    public byte getCode() {
        return 0x8;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public @NotNull OffsetDateTime getTime() {
        return time;
    }

    public @NotNull Path getPath() {
        return path;
    }

    public @NotNull Path getCompletePath() {
        return path.resolve(path);
    }

    public @NotNull String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
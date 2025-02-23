package codes.shawlas.data.message.content.file;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FileCreateMessage implements Message.Input {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull Path path;
    private final @NotNull String name;

    public FileCreateMessage(
            @NotNull UUID id,
            @NotNull OffsetDateTime time,
            @NotNull Path path,
            @NotNull String name
    ) {
        this.id = id;
        this.time = time;
        this.path = path;
        this.name = name;
    }

    @Override
    public byte getCode() {
        return 0x7;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    public @NotNull Path getPath() {
        return path;
    }

    public @NotNull Path getCompletePath() {
        return path.resolve(name);
    }


    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull OffsetDateTime getTime() {
        return time;
    }
}
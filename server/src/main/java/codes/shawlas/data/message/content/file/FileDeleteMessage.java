package codes.shawlas.data.message.content.file;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FileDeleteMessage implements Message.Input {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull Path path;
    private final @NotNull String name;

    public FileDeleteMessage(
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
        return 0x5;
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

    public @NotNull String getName() {
        return name;
    }
}

package codes.shawlas.data.message.content.file;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FolderCreateMessage implements Message.Input {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull Path paths;

    public FolderCreateMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Path paths) {
        this.id = id;
        this.time = time;
        this.paths = paths;
    }

    @Override
    public byte getCode() {
        return 0x6;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public @NotNull OffsetDateTime getTime() {
        return time;
    }

    public @NotNull Path getPaths() {
        return paths;
    }
}

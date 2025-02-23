package codes.shawlas.data.message.content;

import codes.shawlas.data.message.Message;
import codes.shawlas.data.message.MessageWriter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class SuccessMessage implements Message.Input, Message.Output {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull String content;
    private final @NotNull UUID correlationId;

    public SuccessMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Message.Input message, @NotNull String content) {
        this.id = id;
        this.time = time;
        this.content = content;
        this.correlationId = message.getId();
    }

    public SuccessMessage(@NotNull UUID id, @NotNull Message.Input message, @NotNull String content) {
        this.id = id;
        this.time = OffsetDateTime.now();
        this.content = content;
        this.correlationId = message.getId();
    }

    @Override
    public byte getCode() {
        return 0x11;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public @NotNull OffsetDateTime getTime() {
        return time;
    }

    @Override
    public @NotNull ByteBuffer serialize() {
        int size = id.toString().length() + time.toString().length() + content.length() + correlationId.toString().length();

        return new MessageWriter(ByteBuffer.allocate(size + 1))
                .putByte(getCode())
                .putString(id.toString())
                .putString(time.toString())
                .putString(content)
                .putString(correlationId.toString())
                .getBuffer();
    }
}
package codes.shawlas.data.message.content;

import codes.shawlas.data.message.Message;
import codes.shawlas.data.message.MessageWriter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class ExceptionMessage implements Message.Input, Message.Output {

    private final @NotNull UUID id;
    private final @NotNull OffsetDateTime time;
    private final @NotNull UUID correlationId;
    private final @NotNull Throwable exception;

    public ExceptionMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Message.Input message, @NotNull Throwable exception) {
        this.id = id;
        this.time = time;
        this.correlationId = message.getId();
        this.exception = exception;
    }

    public ExceptionMessage(@NotNull UUID id, @NotNull Message.Input message, @NotNull Throwable exception) {
        this.id = id;
        this.time = OffsetDateTime.now();
        this.correlationId = message.getId();
        this.exception = exception;
    }

    @Override
    public byte getCode() {
        return 0x44;
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
        int size = id.toString().length() + time.toString().length() + exception.getMessage().length() + exception.getClass().getSimpleName().length() + correlationId.toString().length();

        return new MessageWriter(ByteBuffer.allocate(size + 1))
                .putByte(getCode())
                .putString(id.toString())
                .putString(time.toString())
                .putString(exception.getClass().getSimpleName())
                .putString(exception.getMessage())
                .putString(correlationId.toString())
                .getBuffer();
    }
}
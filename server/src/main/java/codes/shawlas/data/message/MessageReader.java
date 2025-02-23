package codes.shawlas.data.message;

import codes.shawlas.data.exception.message.MessageReaderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class MessageReader {

    // Static

    public static @NotNull MessageReader getInstance(@NotNull ByteBuffer buffer) {
        return MessageReaders.get(buffer);
    }

    public static @NotNull MessageReader getInstance(byte @NotNull [] bytes) {
        return getInstance(ByteBuffer.wrap(bytes));
    }

    // Objects

    private final byte code;
    private final @NotNull ByteBuffer buffer;

    protected @Nullable Message.Input message;
    protected @Nullable MessageReaderException exception;

    protected MessageReader(byte code, @NotNull ByteBuffer buffer) {
        this.buffer = buffer;
        this.code = code;
    }

    // Getters

    public byte getCode() {
        return code;
    }

    // Abstract

    /**
     * A {@link CompletableFuture} async method for read the message.
     *
     * <p>The future will always completed with {@link Message.Input} if no problems occurs,
     * or with {@link MessageReaderException} if some error occurs*
     * */
    public abstract @NotNull CompletableFuture<Message.Input> nextMessage();

    // Data

    public boolean nextBoolean() {
        return buffer.get() != 0;
    }

    public short nextShort() {
        return buffer.getShort();
    }

    public char nextChar() {
        return buffer.getChar();
    }

    public int nextInt() {
        return buffer.getInt();
    }

    public long nextLong() {
        return buffer.getLong();
    }

    public float nextFloat() {
        return buffer.getFloat();
    }

    public double nextDouble() {
        return buffer.getDouble();
    }

    public @NotNull String nextString() {
        int len = nextShort() & 0xFFF;

        if (buffer.remaining() < len) {
            throw new BufferUnderflowException();
        }

        char @NotNull [] chars = new char[len];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = buffer.getChar();
        }

        return new String(chars);
    }

    public @NotNull UUID nextId() {
        return UUID.fromString(nextString());
    }

    public @NotNull OffsetDateTime nextTime() {
        return OffsetDateTime.parse(nextString());
    }

    // Native

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull MessageReader that = (MessageReader) object;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}

package codes.shawlas.data.utils;

import org.jetbrains.annotations.NotNull;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class MessageUtils {

    public static @NotNull String getString(@NotNull ByteBuffer buffer) {
        int len = buffer.getShort() & 0xFFF;

        if (buffer.remaining() < len) {
            throw new BufferUnderflowException();
        }

        char @NotNull [] chars = new char[len];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = buffer.getChar();
        }

        return new String(chars);
    }

    public static byte getCode(@NotNull ByteBuffer buffer) {
        return buffer.get(0);
    }

    public static @NotNull UUID getId(@NotNull ByteBuffer buffer) {
        return UUID.fromString(getString(buffer));
    }

    public static @NotNull OffsetDateTime getTime(@NotNull ByteBuffer buffer) {
        return OffsetDateTime.parse(getString(buffer));
    }

    public static @NotNull Path getPath(@NotNull ByteBuffer buffer) {
        return Path.of(getString(buffer));
    }

    public static @NotNull ByteBuffer putString(@NotNull ByteBuffer buffer, @NotNull String string) {
        return buffer.put(string.getBytes());
    }

    public static @NotNull ByteBuffer putPath(@NotNull ByteBuffer buffer, @NotNull Path path) {
        return putString(buffer, path.toString());
    }

    public static @NotNull ByteBuffer putTime(@NotNull ByteBuffer buffer, @NotNull OffsetDateTime time) {
        return putString(buffer, time.toString());
    }

    public static @NotNull ByteBuffer putId(@NotNull ByteBuffer buffer, @NotNull UUID id) {
        return putString(buffer, id.toString());
    }

    // Constructor

    private MessageUtils() {
        throw new UnsupportedOperationException();
    }
}

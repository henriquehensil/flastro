package codes.shawlas.data.message;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class MessageWriter {

    private final @NotNull ByteBuffer buffer;

    public MessageWriter(@NotNull ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public @NotNull ByteBuffer getBuffer() {
        return buffer.asReadOnlyBuffer();
    }

    public @NotNull MessageWriter putBytes(byte @NotNull [] bytes) {
        if (bytes.length == 0) throw new IndexOutOfBoundsException();

        for (byte b : bytes) {
            putByte(b);
        }

        return this;
    }

    public @NotNull MessageWriter putByte(byte b) {
        buffer.put(b);
        return this;
    }

    public @NotNull MessageWriter putBoolean(boolean v) {
        buffer.put((byte) (v ? 1 : 0));
        return this;
    }

    public @NotNull MessageWriter putShort(short s) {
        byte high = (byte) (s >> 8), low = (byte) (s & 0xff);
        buffer.put(high).put(low);

        return this;
    }

    public @NotNull MessageWriter putChar(char c) {
        byte high = (byte) (c >> 8), low = (byte) (c & 0xff);
        buffer.put(high).put(low);

        return this;
    }

    public @NotNull MessageWriter putInt(int i) {
        for (int j = 3; j >= 0; j--) {
            buffer.put((byte) ((i >> (j * 8)) & 0xFF));
        }

        return this;
    }

    public @NotNull MessageWriter putLong(long l) {
        for (int j = 7; j >= 0; j--) {
            buffer.put((byte) ((l >> (j * 8)) & 0xFF));
        }

        return this;
    }

    public @NotNull MessageWriter putFloat(float f) {
        return putInt(Float.floatToIntBits(f));
    }

    public @NotNull MessageWriter putDouble(double v) {
        return putLong(Double.doubleToLongBits(v));
    }

    public @NotNull MessageWriter putString(@NotNull String s) {
        return putShort((short) s.length()).putBytes(s.getBytes());
    }
}
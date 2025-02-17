package codes.shawlas.data.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public abstract class ByteMessageReader implements DataInput, Closeable {

    private @Nullable ByteBuffer buffer;
    private volatile boolean close = false;

    protected ByteMessageReader(@NotNull ByteBuffer buffer) {
        if (buffer.isReadOnly()) throw new IllegalArgumentException("Buffer cannot be read-only");
        this.buffer = buffer;
    }

    public int update(@NotNull ReadableByteChannel channel) throws IOException {
        if (close) {
            throw new IOException("Reader is closed");
        } else {
            return channel.read(buffer);
        }
    }

    public final void close() throws IOException {
        if (close) throw new IOException("Reader is already closed");
        buffer = null;
        close = true;
    }

    // Abstract

    public abstract @NotNull CompletableFuture<Void> apply();

    // DataInput implementations

    @Override
    public void readFully(byte @NotNull [] b) throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        buffer.get(b);
    }

    @Override
    public void readFully(byte @NotNull [] b, int off, int len) throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        buffer.get(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        buffer.position(n);
        return buffer.position();
    }

    @Override
    public boolean readBoolean() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return false;
    }

    @Override
    public byte readByte() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.get();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");

        byte b = buffer.get();
        return b >= 0 ? b : b * -1;
    }

    @Override
    public short readShort() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");

        short s = buffer.getShort();
        return s >= 0 ? s : s * -1;
    }

    @Override
    public char readChar() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getChar();
    }

    @Override
    public int readInt() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getInt();
    }

    @Override
    public long readLong() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getLong();
    }

    @Override
    public float readFloat() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getFloat();
    }

    @Override
    public double readDouble() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");
        return buffer.getDouble();
    }

    @Override
    public @Nullable String readLine() throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public @NotNull String readUTF() throws IOException {
        if (close || buffer == null) throw new IOException("Reader is closed");

        int len = buffer.getInt();
        byte @NotNull [] b = new byte[len];

        return new String(b, StandardCharsets.UTF_8);
    }
}
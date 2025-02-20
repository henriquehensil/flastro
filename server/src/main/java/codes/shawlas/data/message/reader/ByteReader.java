package codes.shawlas.data.message.reader;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.message.DataTypeException;
import codes.shawlas.data.exception.message.IllegalMessageException;
import codes.shawlas.data.exception.message.MessageStateException;
import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import codes.shawlas.data.exception.message.MessageReaderException;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class ByteReader implements Comparable<@NotNull ByteReader> {

    // Static initializer

    /**
     * @throws IllegalArgumentException if capacity is negative or code doest not exists
     * */
    public static @NotNull ByteReader fromCode(byte code, int capacity) {
        return ByteReaders.get(code, capacity);
    }

    /**
     * @throws IllegalArgumentException if capacity is negative or code doest not exists
     * */
    public static @NotNull ByteReader fromChannel(@NotNull SocketChannel channel, int capacity) throws IOException, IllegalMessageException {
        @NotNull ByteBuffer buffer = ByteBuffer.allocate(1);
        int read = channel.read(buffer);

        if (read == 0) {
            throw new IllegalMessageException("bytes cannot be null");
        } else if (read == -1) {
            throw new ClosedChannelException();
        } else {
            buffer.flip();
            byte code = buffer.get();
            return fromCode(code, capacity);
        }
    }

    // Objects

    protected final @NotNull ByteBuffer buffer;
    private final byte code;

    /**
     * @throws IllegalArgumentException if capacity is negative
     * */
    protected ByteReader(byte code, int capacity) {
        this.buffer = ByteBuffer.allocate(capacity);
        this.code = code;
    }

    // Abstract

    public abstract @NotNull MessageReader getMessageReader();

    // Getters

    public int getCapacity() {
        return buffer.capacity();
    }

    public byte getCode() {
        return code;
    }

    public int update(@NotNull ReadableByteChannel channel) throws IOException {
        buffer.clear();

        int read = channel.read(buffer);

        if (read == -1) throw new ClosedChannelException();
        buffer.flip();

        return read;
    }

    // Comparable implementation

    @Override
    public int compareTo(@NotNull ByteReader o) {
        return Integer.compare(getCapacity(), o.getCapacity());
    }

    // Classes

    public abstract class MessageReader {

        protected @Nullable Message.Input message;
        protected @Nullable MessageReaderException exception;

        /**
         * @return An input message or {@link IllegalMessageException} if some error occurs
         * */
        public @NotNull Message.Input read() throws IllegalMessageException {
            if (message != null) {
                return message;
            } else if (exception != null) {
                throw new IllegalMessageException("The message has an illegal format", exception);
            } else try {
                @NotNull Message.Input message = nextMessage().join();
                return message;
            } catch (CompletionException e) {
                throw new RuntimeException("The message has illegal format", e);
            } catch (CancellationException e) {
                throw new RuntimeException("The executor was canceled for some reason", e);
            }
        }

        // Abstract

        /**
         * A {@link CompletableFuture} async method for read the message.
         *
         * <p>The future will always completed with {@link Message.Input} if no problems occurs,
         * or with {@link MessageReaderException} if some error occurs*
         * */
        protected abstract @NotNull CompletableFuture<Message.Input> nextMessage();

        /**
         * @throws MessageStateException if the {@link MessageReader#read()} was not invoked or throws an {@link IllegalMessageException}
         * */
        public abstract @NotNull MessageExecutor getExecutor(@NotNull Database database) throws MessageStateException;

        // Utils

        /**
         * @throws IndexOutOfBoundsException If there are fewer than length bytes remaining
         * */
        public void next(byte @NotNull [] b) throws IOException {
            next(b, 0, b.length);
        }

        /**
         * @throws IndexOutOfBoundsException If the offset and length parameters are illegal
         * @throws EOFException If there are fewer than length bytes remaining in this buffer
         * */
        public void next(byte @NotNull [] b, int off, int len) throws IOException {
            try {
                buffer.get(b, off, len);
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public boolean nextBoolean() throws EOFException {
            try {
                return nextByte() != 0;
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public byte nextByte() throws EOFException {
            try {
                return buffer.get();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public short nextShort() throws EOFException {
            try {
                return buffer.getShort();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public char nextChar() throws EOFException {
            try {
                return buffer.getChar();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public int nextInt() throws EOFException {
            try {
                return buffer.getInt();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public long nextLong() throws EOFException {
            try {
                return buffer.getLong();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public float nextFloat() throws EOFException {
            try {
                return buffer.getFloat();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public double nextDouble() throws EOFException {
            try {
                return buffer.getDouble();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        public @NotNull String nextString() throws IOException {
            short len = nextShort();

            if (len < 0) {
                throw new DataTypeException("Cannot find the length string");
            } else if (buffer.remaining() < len) {
                throw new DataTypeException("Insufficient bytes in buffer");
            } else {
                byte @NotNull [] bytes = new byte[len];
                next(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
    }

    // Executor

    public abstract static class MessageExecutor {

        protected final @NotNull Message.Input message;
        protected final @NotNull Database database;

        protected MessageExecutor(@NotNull Message.Input message, @NotNull Database database) {
            this.message = message;
            this.database = database;
        }

        public abstract @NotNull CompletableFuture<Void> execute();
    }
}
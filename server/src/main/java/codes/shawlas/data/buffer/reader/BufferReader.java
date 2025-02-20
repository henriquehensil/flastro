package codes.shawlas.data.buffer.reader;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.buffer.MinimumBytesException;
import codes.shawlas.data.exception.message.DataTypeException;
import codes.shawlas.data.exception.message.IllegalMessageException;
import codes.shawlas.data.exception.message.MessageStateException;
import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import codes.shawlas.data.exception.message.MessageReaderException;
import codes.shawlas.data.exception.message.MessageExecutorException;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract sealed class BufferReader implements Comparable<@NotNull BufferReader> permits FileUploadReader {

    // Static initializer

    public static @NotNull BufferReader allocate(byte code, int capacity) {
        return BufferReaders.get(code, capacity);
    }

    public static @NotNull BufferReader allocate(byte @NotNull [] bytes) {
        if (bytes.length == 0) {
            throw new MinimumBytesException("Cannot accepted null bytes");
        }

        return BufferReaders.get(bytes, false);
    }

    public static @NotNull BufferReader allocate(@NotNull ByteBuffer buffer) {
        return allocate(buffer.array());
    }

    /**
     * Allocates an independent buffer reader.
     *
     * <p>An independent buffer reader automatically executes the {@link MessageReader#read()} method asynchronously.
     *
     * <p>If the given parameter doest not have sufficient data, the read method will throw an {@link IllegalMessageException}
     * and {@link MessageReader#getExecutor(Database)} will become unsupported.
     *
     * <p>Use this when you are certain that there is sufficient data.
     * */
    public static @NotNull BufferReader allocateIndependent(byte @NotNull [] bytes) {
        if (bytes.length == 0) {
            throw new MinimumBytesException("Cannot accepted null bytes");
        }

        return BufferReaders.get(bytes, true);
    }

    /**
     * Allocates an independent buffer reader.
     *
     * <p>An independent buffer reader automatically executes the {@link MessageReader#read()} method asynchronously.
     *
     * <p>If the given parameter doest not have sufficient data, the read method will throw an {@link IllegalMessageException}
     * and {@link MessageReader#getExecutor(Database)} will become unsupported.
     *
     * <p>Use this when you are certain that there is sufficient data.
     * */
    public static @NotNull BufferReader allocateIndependent(@NotNull ByteBuffer buffer) {
        return allocateIndependent(buffer.array());
    }

    // Objects

    protected final @NotNull ByteBuffer buffer;
    private final byte code;

    /**
     * Create an empty buffer reader.
     *
     * @throws IllegalArgumentException if capacity is negative
     * */
    protected BufferReader(byte code, int capacity) {
        this.buffer = ByteBuffer.allocate(capacity);
        this.code = code;
    }

    protected BufferReader(byte @NotNull [] bytes, boolean independent) {
        this.code = bytes[0];
        this.buffer = ByteBuffer.wrap(bytes);

        if (independent) {
            getReader().nextMessage();
        }
    }

    protected BufferReader(@NotNull ByteBuffer byteBuffer, boolean independent) {
        this(byteBuffer.array(), independent);
    }

    // Abstract

    public abstract @NotNull MessageReader getReader();

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

        return read;
    }

    public int write(@NotNull WritableByteChannel channel) throws IOException {
        buffer.flip();
        return channel.write(buffer);
    }

    // Comparable implementation

    @Override
    public int compareTo(@NotNull BufferReader o) {
        return Integer.compare(getCapacity(), o.getCapacity());
    }

    // Classes

    public abstract sealed class MessageReader permits FileUploadReader.Reader {

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
        protected void next(byte @NotNull [] b, int off, int len) throws IOException {
            try {
                buffer.get(b, off, len);
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected boolean nextBoolean() throws EOFException {
            try {
                return nextByte() != 0;
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected byte nextByte() throws EOFException {
            try {
                return buffer.get();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected short nextShort() throws EOFException {
            try {
                return buffer.getShort();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected char nextChar() throws EOFException {
            try {
                return buffer.getChar();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected int nextInt() throws EOFException {
            try {
                return buffer.getInt();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected long nextLong() throws EOFException {
            try {
                return buffer.getLong();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected float nextFloat() throws EOFException {
            try {
                return buffer.getFloat();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected double nextDouble() throws EOFException {
            try {
                return buffer.getDouble();
            } catch (BufferOverflowException e) {
                throw new EOFException(e.getMessage());
            }
        }

        protected @NotNull String nextString() throws IOException {
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

        protected @NotNull UUID nextId() throws IOException {
            return UUID.fromString(nextString());
        }

        protected @NotNull OffsetDateTime nextTime() throws IOException {
            return OffsetDateTime.parse(nextString());
        }

        // Classes

        public abstract sealed class MessageExecutor permits FileUploadReader.Reader.Executor {

            protected final @NotNull Message.Input message;
            protected final @NotNull Database database;

            protected MessageExecutor(@NotNull Database database) {
                @Nullable Message.Input message = MessageReader.this.message;

                if (message == null) {
                    if (exception == null) {
                        throw new MessageStateException("The message has been not read");
                    } else {
                        throw new MessageStateException("The message is invalid", exception);
                    }
                }

                this.database = database;
                this.message = message;
            }

            /**
             * A {@link CompletableFuture} async method for execute the message.
             *
             * <p>The future will always completed with {@link MessageExecutorException} if some error occurs.
             *
             * @param channel The channel used to update the buffer reader if needed.
             * */
            public abstract @NotNull CompletableFuture<Void> execute(@NotNull ReadableByteChannel channel);
        }
    }
}
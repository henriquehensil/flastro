package dev.hensil.flastro.core.storage.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

final class ChunkFile implements Closeable {

    // Objects

    private @Nullable RandomAccessFile file;
    private final @NotNull Data data;
    private final @NotNull Blocks blocks;

    private volatile boolean closed = false;

    private ChunkFile(@NotNull RandomAccessFile file, @NotNull ByteBuffer blocks, int dataLength) {
        this.file = file;
        this.data = new Data(Chunk.CHUNK_MAX_LENGTH, Chunk.BLOCK_MAX_LENGTH, dataLength);
        this.blocks = new Blocks(dataLength, blocks);
    }

    public void write(byte @NotNull [] bytes) throws IOException {
        checkIfClosed();

        data.append(bytes);
        blocks.setStart(data.length);
    }

    public void write(int index, byte @NotNull [] bytes, int length) throws IOException {
        checkIfClosed();
        data.replaces(index, bytes, length);
    }

    public void add(@NotNull Chunk.Block block) {
        checkIfClosed();
        blocks.add(block);
    }

    public void replace(int pos, @NotNull Chunk.Block block) {
        checkIfClosed();
        blocks.replace(pos, block);
    }

    private void write0(int index, byte @NotNull [] data, int length) throws IOException {
        checkIfClosed();
        assert file != null;

        file.seek(index);
        file.write(data, 0, length);
    }

    private void checkIfClosed() {
        if (closed) {
            throw new IllegalStateException("Chunk File is closed");
        }
    }

    public int dataLength() {
        return data.length;
    }

    public int dataRemaining() {
        return data.remaining();
    }

    public boolean dataIsFull() {
        return data.isFull();
    }

    public int blocksWritten() {
        return blocks.size();
    }

    @Override
    public void close() {
        blocks.close();
        file = null;
        closed = true;
    }

    // Classes

    private final class Data {

        private final int dataLimit;
        private final int writeLimit;
        private int length;

        private Data(int dataLimit, int writeLimit, int length) {
            this.dataLimit = dataLimit;
            this.writeLimit = writeLimit;
            this.length = length;
        }

        public void append(byte @NotNull [] data) throws IOException {
            if (isFull()) {
                throw new IllegalStateException("The chunk data file is full");
            }

            int len = getWrittableBytes(data);
            write0(length, data, len);
            length += len;
        }

        public void replaces(int index, byte @NotNull [] data, int length) throws IOException {
            if (this.length - index < length) {
                throw new IllegalArgumentException("Cannot exceed the data length");
            }

            write0(index, data, length);
        }

        public int getWrittableBytes(byte @NotNull [] bytes) {
            return Math.min(remaining(), Math.min(bytes.length, writeLimit));
        }

        public boolean isFull() {
            return remaining() == 0;
        }

        public int remaining() {
            return dataLimit - length;
        }
    }

    private final class Blocks implements Closeable {

        // Static initializers

        private static final int BLOCKS_LENGTH = 9;

        private static byte @NotNull [] serialize(@NotNull Chunk.Block block) {
            return ByteBuffer.allocate(BLOCKS_LENGTH).clear()
                    .putInt(block.getIndex())
                    .putInt(block.getLength())
                    .put(block.isSuppressed() ? (byte) 0 : (byte) 1)
                    .array();
        }

        // Objects

        private @Nullable ByteBuffer buffer;
        private int start;
        private int count;

        private Blocks(int start, @NotNull ByteBuffer buffer) {
            this.start = start;
            this.buffer = buffer;
        }

        public void add(@NotNull Chunk.Block block) {
            assert buffer != null;
            buffer.clear()
                    .position(count * BLOCKS_LENGTH)
                    .put(serialize(block));

            count++;
        }

        public void replace(int pos, @NotNull Chunk.Block block) {
            assert buffer != null;

            buffer.flip()
                    .position(pos);

            int index = buffer.getInt();
            int length = buffer.getInt();

            if (!equals(index, length, block)) {
                throw new IllegalArgumentException();
            }

            buffer.position(pos)
                    .put(serialize(block))
                    .clear();
        }

        private boolean equals(int index, int length, @NotNull Chunk.Block block) {
            return Objects.hash(index, length) == block.hashCode();
        }

        public void setStart(int start) throws IOException {
            this.start = start;
            update();
        }

        private void update() throws IOException {
            assert file != null && buffer != null;

            buffer.flip();

            write0(start, buffer.array(), buffer.limit());
            file.writeInt(size());

            buffer.clear();
        }

        public int size() {
            return count;
        }

        @Override
        public void close() {
            if (buffer == null) {
                return;
            }

            buffer = null;
        }
    }
}
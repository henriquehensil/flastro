package dev.hensil.flastro.core.storage.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class ChunkFile implements Closeable {

    static final int DATA_MAX_SIZE = 512 * 1024 * 1024; // 512MB
    static final int BLOCK_MAX_SIZE = 16 * 1024 * 1024; // 16MB

    public static @NotNull ChunkFile recent(@NotNull Path path) throws IOException {
        Files.deleteIfExists(path);

        return new ChunkFile(path, new HashMap<>(), 0);
    }

    public static byte @NotNull [] serialize(@NotNull Chunk.Block block) {
        @NotNull ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putInt(block.getIndex()).putInt(block.getLength()).put(block.isSuppressed() ? (byte) 0x1 : (byte) 0x0);

        return buffer.array();
    }

    private static void createFile(@NotNull Path path) throws IOException {
        Files.createDirectories(path.getParent());

        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignore) {
            //
        }
    }

    // Objects

    private final @NotNull Path path;
    private @Nullable RandomAccessFile file;
    private final @NotNull Blocks blocks;
    private final @NotNull Data data;
    private int length;

    private final @NotNull Object lock = new Object();
    private volatile boolean closed;

    private ChunkFile(@NotNull Path path, @NotNull Map<Chunk.@NotNull Block, @NotNull Integer> blocks, int length) throws IOException {
        createFile(path);

        this.path = path;
        this.file = new RandomAccessFile(path.toFile(), "rwd");

        this.blocks = new Blocks(blocks);
        this.data = new Data(length);
    }

    // Getters

    public void write(byte @NotNull [] bytes) throws IOException {
        checkIfClosed();

        synchronized (lock) {
            this.length += data.append(bytes);
            this.blocks.update();
        }
    }

    public void write(int index, byte @NotNull [] bytes, int length) throws IOException {
        checkIfClosed();

        synchronized (lock) {
            data.rewrite(index, bytes, length);
        }
    }

    public void write(@NotNull Chunk.Block block) throws IOException {
        checkIfClosed();

        synchronized (lock) {
            this.length += this.blocks.write(block);
        }
    }

    public void invert(@NotNull Chunk.Block block) throws IOException {
        checkIfClosed();

        synchronized (lock) {
            this.blocks.invertAndWrite(block);
        }
    }

    public @NotNull Path getPath() {
        return path;
    }

    public int getTotalLength() {
        return length;
    }

    public int getDataLength() {
        return data.length();
    }

    public int getDataRemaining() {
        return data.getRemaining();
    }

    public boolean dataIsFull() {
        return data.isFull();
    }

    @Override
    public void close() throws IOException {
        if (closed || file == null) {
            return;
        }

        synchronized (lock) {
            closed = true;
            file.close();
            file = null;
        }
    }

    private void checkIfClosed() throws IOException {
        if (closed) {
            throw new IOException("Chunk file is closed");
        }
    }

    void deleteFile() throws IOException {
        close();
        Files.delete(path);
    }

    // Classes

    private final class Blocks {

        private final @NotNull Map<Chunk.@NotNull Block, @NotNull Integer> blocksMap;

        private Blocks(@NotNull Map<Chunk.@NotNull Block, @NotNull Integer> blocksMap) {
            this.blocksMap = blocksMap;
        }

        public int write(@NotNull Chunk.Block block) throws IOException {
            if (blocksMap.containsKey(block)) {
                throw new IllegalArgumentException("Block already written");
            }

            assert file != null;

            byte @NotNull [] bytes = serialize(block);

            if (blocksMap.isEmpty()) {
                file.seek(data.length());
                file.write(bytes);
                file.writeInt(1); // offset

                blocksMap.put(block, 1);

                return bytes.length;
            }

            int index = length;
            file.seek(index);
            file.write(bytes);

            int size = blocksMap.size() + 1;
            file.writeInt(size); // offset

            blocksMap.put(block, size);

            return bytes.length;
        }

        public void invertAndWrite(@NotNull Chunk.Block block) throws IOException {
            assert file != null;

            int pos = blocksMap.getOrDefault(block, -1);
            if (pos == -1) throw new IllegalArgumentException("Nom-member block: " + block);

            @NotNull Chunk.Block newBlock = block.invert();
            byte @NotNull [] bytes = serialize(newBlock);

            int start = data.length();
            int index = start + pos * bytes.length;
            file.seek(index);
            file.write(bytes);

            blocksMap.remove(block);
            blocksMap.put(newBlock, pos);
        }

        private void update() throws IOException {
            assert file != null;

            int start = data.length();
            file.seek(start);

            for (@NotNull Chunk.Block block : blocksMap.keySet()) {
                file.write(serialize(block));
            }

            file.writeInt(blocksMap.size()); // offset
        }
    }

    private final class Data {

        private final @NotNull AtomicInteger length;

        private Data(int length) {
            this.length = new AtomicInteger(length);
        }

        public int append(byte @NotNull [] data) throws IOException {
            int len = getWritableLength(data);
            if (len == 0) throw new IllegalStateException("Data chunk is full");

            assert file != null;

            file.seek(this.length.get());
            file.write(data, 0, len);
            this.length.addAndGet(len);

            return len;
        }

        public void rewrite(int index, byte @NotNull [] bytes, int length) throws IOException {
            if (index < 0) {
                throw new IllegalArgumentException("Invalid index value");
            } else if (length > BLOCK_MAX_SIZE) {
                throw new IllegalArgumentException("Length exceed the block max length");
            } else if (index + length > this.length.get()) {
                throw new IllegalArgumentException("Data would exceed the data length");
            } else {
                assert file != null;

                file.seek(index);
                file.write(bytes, 0, length);
            }
        }

        public int getWritableLength(byte @NotNull [] bytes) {
            return Math.min(getRemaining(), Math.min(bytes.length, BLOCK_MAX_SIZE));
        }

        public boolean isFull() {
            return getRemaining() == 0;
        }

        public int getRemaining() {
            return DATA_MAX_SIZE - length();
        }

        public int length() {
            return length.get();
        }
    }
}
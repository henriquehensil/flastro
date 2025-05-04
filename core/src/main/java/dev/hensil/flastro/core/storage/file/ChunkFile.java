package dev.hensil.flastro.core.storage.file;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

final class ChunkFile implements Closeable {

    private static int DATA_MAX_SIZE = 512 * 1024 * 1024; // 512MB
    private static int BLOCK_MAX_SIZE = 16 * 1024 * 1024; // 16MB
    private static int BLOCKS_LENGTH = 9;

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
    private final @NotNull RandomAccessFile file;
    private final @NotNull Blocks blocks;
    private final @NotNull Data data;

    private ChunkFile(@NotNull Path path, @NotNull Map<@NotNull Integer, @NotNull Integer> blocks, int length) throws IOException {
        createFile(path);
        this.path = path;
        this.file = new RandomAccessFile(path.toFile(), "rwd");
        this.blocks = new Blocks(blocks);
        this.data = new Data(length);
    }

    // Getters

    public @NotNull Path getPath() {
        return path;
    }

    public @NotNull Blocks getBlocks() {
        return blocks;
    }

    public @NotNull Data getData() {
        return data;
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            file.close();
        }
    }

    // Classes

    final class Blocks {

        private final @NotNull Map<@NotNull Integer, @NotNull Integer> blocksMap;
        private int start;

        private Blocks(@NotNull Map<@NotNull Integer, @NotNull Integer> blocksMap) {
            this.blocksMap = blocksMap;
        }

        public void write(@NotNull Chunk.Block block) throws IOException {
            if (blocksMap.containsKey(block.hashCode())) {
                return;
            }

            file.seek(start);
            file.write(serialize(block));
        }

        public void write(@NotNull Chunk.Block old, @NotNull Chunk.Block newBlock) throws IOException {
            int hash = old.hashCode();
            int pos = blocksMap.getOrDefault(hash, -1);

            if (pos == -1) {
                throw new IllegalArgumentException("Cannot find the block hashcode");
            }

            byte @NotNull [] bytes = serialize(newBlock);
            int index = start + pos * bytes.length;
            file.seek(index);
            file.write(bytes);
        }
    }

    final class Data {

        private int length;

        private Data(int length) {
            this.length = length;
        }

        public void write(byte @NotNull [] data) throws IOException {
            int len = getWritableLength(data);
            if (len == 0) {
                throw new IllegalStateException("Data chunk is full");
            }

            file.seek(this.length);
            file.write(data, 0, len);
            this.length += len;
        }

        public void write(int index, byte @NotNull [] bytes, int length) throws IOException {
            if (index < 0) {
                throw new IllegalArgumentException("Invalid index value");
            } else if (index + length > this.length) {
                throw new IllegalArgumentException("Data would exceed the data length");
            } else {
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
            return DATA_MAX_SIZE - length;
        }

        public int length() {
            return length;
        }
    }
}
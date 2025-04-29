package dev.hensil.flastro.core.storage.file;

import dev.hensil.flastro.core.exception.file.FullChunkException;
import dev.hensil.flastro.core.exception.file.NoSuchBlockException;
import dev.hensil.flastro.core.exception.file.SuppressedBlockException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

//todo ChunkStream
public final class Chunk implements Iterable<Chunk.@NotNull Block> {

    static final int MAX_SIZE = 512 * 1024 * 1024; // 512MB

    // Objects

    public static @NotNull Chunk recent(@NotNull Path data, @NotNull Path metadata) throws IOException {
        return new Chunk(data, metadata);
    }

    public static @NotNull Chunk load(@NotNull Path dataPath, @NotNull Path metaPath) throws IOException {
        try (@NotNull DataInputStream file = new DataInputStream(Files.newInputStream(metaPath))) {

            int normal = file.readInt();
            int suppressed = file.readInt();
            if (normal < 0 || suppressed < 0) {
                throw new IOException("Illegal block length in metadata");
            }

            System.out.println("normal blocks: " + normal);
            System.out.println("suppressed blocks: " + suppressed);

            int metadataLength = 8;
            int dataLength = 0;
            @NotNull Map<@NotNull Block, @NotNull Integer> indexes = new HashMap<>(normal + suppressed);
            @NotNull Set<@NotNull Block> normalBlocks = new HashSet<>(normal);
            @NotNull Set<@NotNull Block> suppressedBlocks = new HashSet<>(suppressed);

            for (int i = 0; i < (normal + suppressed); i++) {
                @NotNull Block block = readBlock(file);
                indexes.put(block, block.index);

                if (block.isSuppressed()) {
                    suppressedBlocks.add(block);
                } else {
                    normalBlocks.add(block);
                }

                dataLength += block.length();
                metadataLength += block.bytesLength;
            }

            if (dataLength != Files.size(dataPath)) {
                throw new IOException("The data path size is different that was read in metadata path");
            } else if (metadataLength != Files.size(metaPath)) {
                throw new IOException("Metadata path has illegal bytes");
            } else
                return new Chunk(new Data(dataPath, dataLength), new Meta(metaPath, indexes, normal, suppressed), normalBlocks, suppressedBlocks);
        }
    }

    private static @NotNull Block readBlock(@NotNull DataInputStream file) throws IOException {
        int index = file.readInt();
        int length = file.readInt();
        byte suppressed = file.readByte();

        System.out.println(suppressed);

        if (index < 0) {
            throw new IOException("Invalid index value: " + index);
        } else if (length < 0) {
            throw new IOException("Invalid length value: " + length);
        } else if (!(suppressed >= 0 && suppressed <= 1)) {
            throw new IOException("Illegal suppressed value");
        } else
            return new Block(index, length, suppressed == 1);
    }

    private static void createFile(@NotNull Path path) throws IOException {
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignore) {
            //
        }
    }

    // Todo synchronized

    private final @NotNull Data data;
    private final @NotNull Meta meta;

    private final @NotNull Set<@NotNull Block> normalBlocks, suppressedBlocks;

    private Chunk(@NotNull Path dataPath, @NotNull Path metadataPath) throws IOException {
        this(new Data(dataPath), new Meta(metadataPath), new HashSet<>(), new HashSet<>());
    }

    private Chunk(@NotNull Data data, @NotNull Meta meta, @NotNull Set<@NotNull Block> blocks, @NotNull Set<@NotNull Block> suppressedBlocks) {
        this.data = data;
        this.meta = meta;

        this.normalBlocks = blocks;
        this.suppressedBlocks = suppressedBlocks;
    }

    public @NotNull Path getDataPath() {
        return data.path;
    }

    public @NotNull Path getMetaPath() {
        return meta.path;
    }

    public @NotNull Block write(byte @NotNull [] bytes) throws IOException {
        if (isFull()) {
            throw new FullChunkException("The chunk is already full: " + length());
        }

        if (hasSuppressed()) {
            for (@NotNull Block sb : suppressedBlocks) {
                if (sb.canRewrite(bytes)) {
                    @NotNull Block block = new Block(sb.index, sb.length, false);
                    data.write(bytes, sb);
                    meta.replace(sb, block);

                    suppressedBlocks.remove(sb);
                    normalBlocks.add(block);

                    return block;
                }
            }
        }

        int thatIndex = length();
        int length = data.write(bytes);

        @NotNull Block block = new Block(thatIndex, length, false);

        meta.write(block);

        normalBlocks.add(block);
        return block;
    }

    public @NotNull Block delete(@NotNull Block block) throws IOException {
        if (!normalBlocks.contains(block) && !suppressedBlocks.contains(block)) {
            throw new NoSuchBlockException("Non-member block: " + block);
        } else if (block.isSuppressed()) {
            throw new SuppressedBlockException("This block is already suppressed: " + block);
        } else {
            @NotNull Block suppressedBlock = new Block(block.index, block.length, true);

            normalBlocks.remove(block);
            suppressedBlocks.add(suppressedBlock);
            meta.replace(block, suppressedBlock);

            return suppressedBlock;
        }
    }

    // Getters

    public int length() {
        return data.length;
    }

    public boolean has(@NotNull Block block) {
        return normalBlocks.contains(block) || suppressedBlocks.contains(block);
    }

    public boolean isFull() {
        return data.length == Chunk.MAX_SIZE;
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public int getRemaining() {
        return Chunk.MAX_SIZE - data.length;
    }

    public @Unmodifiable @NotNull Set<@NotNull Block> getBlocks() {
        return Collections.unmodifiableSet(normalBlocks);
    }

    public @Unmodifiable @NotNull Set<@NotNull Block> getSuppressedBlocks() {
        return Collections.unmodifiableSet(suppressedBlocks);
    }

    public boolean hasSuppressed() {
        return !suppressedBlocks.isEmpty();
    }

    @Override
    public @NotNull Iterator<@NotNull Block> iterator() {
        return normalBlocks.iterator();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull Chunk chunk = (Chunk) object;
        return Objects.equals(data, chunk.data) &&
                Objects.equals(meta, chunk.meta) &&
                Objects.equals(normalBlocks, chunk.normalBlocks) &&
                Objects.equals(suppressedBlocks, chunk.suppressedBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta, normalBlocks, suppressedBlocks);
    }

    @SuppressWarnings("warning")
    void deleteFile() throws IOException {
        data.close();
        meta.close();
        Files.delete(getDataPath());
        Files.delete(getMetaPath());
    }

    // Classes

    public static final class Block implements Comparable<@NotNull Block> {

        private static final int MAX_SIZE = 16 * 1024 * 1024; // 16 MB

        // Objects

        private final int index;
        private final int length;
        private final boolean suppressed;

        private final int bytesLength = 9;

        private Block(int index, int length, boolean suppressed) {
            if (length > MAX_SIZE) throw new UnsupportedOperationException("Block length exceeded the max size block: " + length);
            this.index = index;
            this.length = length;
            this.suppressed = suppressed;
        }

        // Getters

        public boolean canRewrite(byte @NotNull [] data) {
            return suppressed && data.length >= this.length;
        }

        public int length() {
            return length;
        }

        public int index() {
            return index;
        }

        public boolean isSuppressed() {
            return suppressed;
        }

        private byte @NotNull [] toBytes() {
            @NotNull ByteBuffer buffer = ByteBuffer.allocate(bytesLength);
            buffer.putInt(index);
            buffer.putInt(length);
            buffer.put(suppressed ? (byte) 1 : (byte) 0);

            return buffer.array();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Block block = (Block) object;
            return index == block.index && length == block.length && suppressed == block.suppressed;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, length, suppressed);
        }

        @Override
        public int compareTo(@NotNull Block o) {
            return Integer.compare(index, o.index);
        }
    }

    // Classes

    private static final class Data implements Closeable {

        private final @NotNull Path path;
        private final @NotNull RandomAccessFile file;
        private int length;

        private Data(@NotNull Path path) throws IOException {
            this(path, 0);
        }

        private Data(@NotNull Path path, int size) throws IOException {
            createFile(path);
            this.path = path;
            this.file = new RandomAccessFile(path.toFile(), "rwd");
            this.length = size;
        }

        public int write(byte @NotNull [] data) throws IOException {
            file.seek(length);

            int real = Math.min(getRemaining(), Math.min(data.length, Block.MAX_SIZE));
            file.write(data, 0, real);
            this.length += real;

            return real;
        }

        public void write(byte @NotNull [] data, @NotNull Block block) throws IOException {
            file.seek(block.index);
            file.write(data, 0, block.length);
        }

        public int getRemaining() {
            return Chunk.MAX_SIZE - length;
        }

        @Override
        public void close() throws IOException {
            file.close();
        }

        // Native

        @Override
        public boolean equals(@Nullable Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Data data = (Data) object;
            return length == data.length && Objects.equals(path, data.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, length);
        }
    }

    private static final class Meta implements Closeable {

        private final @NotNull Path path;
        private final @NotNull RandomAccessFile file;
        private final @NotNull Map<@NotNull Block, @NotNull Integer> indexes;
        private int length;

        private int normalBlocksSize;
        private int suppressedBlocksSize;

        private Meta(@NotNull Path path) throws IOException {
            this(path, new HashMap<>(), 0, 0);

            file.seek(0);
            file.write(normalBlocksSize);

            file.seek(4);
            file.write(suppressedBlocksSize);
        }

        private Meta(@NotNull Path path, @NotNull Map<@NotNull Block, @NotNull Integer> indexes, int normalBlocksSize, int suppressedBlocksSize) throws IOException {
            createFile(path);

            this.path = path;
            this.file = new RandomAccessFile(path.toFile(), "rwd");
            this.indexes = indexes;

            this.normalBlocksSize = normalBlocksSize;
            this.suppressedBlocksSize = suppressedBlocksSize;

            this.length = 0;
        }

        public void write(@NotNull Block block) throws IOException {
            if (indexes.containsKey(block)) {
                throw new IllegalArgumentException("Block already exists: " + block);
            }

            int pos = length;
            file.seek(pos);
            byte @NotNull [] bytes = block.toBytes();
            file.write(bytes);
            length += bytes.length;

            indexes.put(block, pos);

            updateSize(block, true);
        }

        public void replace(@NotNull Block old, @NotNull Block block) throws IOException {
            if (checkReplace(old, block)) {
                throw new IllegalArgumentException("Cannot replace these blocks");
            } else {
                int index = indexes.getOrDefault(old, -1);
                if (index == -1) {
                    throw new AssertionError("Internal error: Blocks doest not contained on indexes map");
                }

                file.seek(index);
                file.write(block.toBytes());

                if (updateSize(old, false) && updateSize(block, true)) {
                    indexes.remove(old);
                    indexes.put(block, index);
                }
            }
        }

        private boolean checkReplace(@NotNull Block block1, @NotNull Block block2) {
            return block1.index == block2.index && block1.length == block2.length && block1.suppressed != block2.suppressed;
        }

        private boolean updateSize(@NotNull Block block, boolean add) throws IOException {
            int oldNormal = normalBlocksSize;
            int oldSuppressed = suppressedBlocksSize;

            if (block.isSuppressed()) {
                if (add) {
                    suppressedBlocksSize += 1;
                } else {
                    suppressedBlocksSize -= 1;
                }
            } else {
                if (add) {
                    normalBlocksSize += 1;
                } else {
                    normalBlocksSize -= 1;
                }
            }

            try {
                file.seek(0);
                file.write(normalBlocksSize);
                file.seek(4);
                file.write(suppressedBlocksSize);
                return true;
            } catch (Throwable e) {
                normalBlocksSize = oldNormal;
                suppressedBlocksSize = oldSuppressed;
                return false;
            }
        }

        @Override
        public void close() throws IOException {
            file.close();
        }

        // Native

        @Override
        public boolean equals(@Nullable Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Meta meta = (Meta) object;
            return Objects.equals(path, meta.path) && Objects.equals(indexes, meta.indexes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, indexes);
        }
    }
}
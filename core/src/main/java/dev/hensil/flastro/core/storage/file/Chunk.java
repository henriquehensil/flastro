package dev.hensil.flastro.core.storage.file;

import dev.hensil.flastro.core.exception.file.FullChunkException;
import dev.hensil.flastro.core.exception.file.NoSuchSuppressedBlockException;
import dev.hensil.flastro.core.exception.file.SuppressedBlockException;
import dev.hensil.flastro.core.exception.file.NoSuchBlockException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

//todo ChunkStream
public final class Chunk implements Iterable<Chunk.@NotNull Block> {

    static final int MAX_SIZE = 512 * 1024 * 1024; // 512MB

    static @NotNull Chunk recentChuck(@NotNull Path path) throws IOException {
        return new Chunk(
                path,
                new LinkedHashSet<>(512, 0.5f),
                new RandomAccessFile(path.toString(), "rwd"),
                0,
                false
        );
    }

    // Objects

    private final @NotNull Path path;
    private final @NotNull Set<@NotNull Block> blocks;

    private final @NotNull RandomAccessFile file;
    private int size;

    private volatile int offset;
    private volatile boolean suppressed;

    private Chunk(
            @NotNull Path path,
            @NotNull Set<@NotNull Block> blocks,
            @NotNull RandomAccessFile file,
            int offset,
            boolean hasSuppressed
    )
            throws IOException
    {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        } else if (!Files.isRegularFile(path)) {
            throw new FileNotFoundException("File must to be a regular file: " + path);
        }

        this.path = path;
        this.blocks = blocks;
        this.offset = offset;

        this.file = file;
        this.file.seek(offset);

        this.suppressed = hasSuppressed;
    }

    public @NotNull Path getPath() {
        return path;
    }

    public boolean has(@NotNull Block block) {
        synchronized (this) {
            return blocks.contains(block);
        }
    }

    public boolean isFull() {
        return size == Chunk.MAX_SIZE;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int getRemaining() {
        return MAX_SIZE - size;
    }

    public @NotNull Block write(byte @NotNull [] data) throws IOException {
        if (isFull()) {
            throw new FullChunkException("The chunk is already full: " + size);
        }

        int lastIndex = offset;
        writeData(data);
        int length = offset - lastIndex;

        synchronized (this) {
            @NotNull Block block = new Block(this, lastIndex, length, false);
            blocks.add(block);
            return block;
        }
    }

    public @NotNull Block rewrite(byte @NotNull [] data) throws IOException {
        synchronized (this) {
            if (!hasSuppressed()) {
                throw new NoSuchSuppressedBlockException("No suppressed block");
            }

            @NotNull Set<@NotNull Block> suppressedBlocks = getSuppressedBlocks();

            for (@NotNull Block block : suppressedBlocks) {
                if (block.canWrite(data)) {
                    file.seek(block.index);
                    file.write(data, 0, block.length);
                    offset = (int) file.getFilePointer();

                    @NotNull Block b = new Block(this, offset, block.length, false);
                    blocks.remove(block);
                    blocks.add(b);

                    if (suppressedBlocks.size() == 1) {
                        suppressed = false;
                    }

                    return b;
                }
            }

            throw new NoSuchSuppressedBlockException("Cannot find the ideal suppressed block");
        }
    }

    public @NotNull Block delete(@NotNull Block block) {
        if (!has(block)) {
            throw new NoSuchBlockException("Non-member block: " + block);
        } else if (block.isSuppressed()) {
            throw new SuppressedBlockException("This block is already suppressed: " + block);
        } else synchronized (this) {
            @NotNull Block suppressedBlock = new Block(this, block.index, block.length, true);

            suppressed = true;
            blocks.remove(block);
            blocks.add(suppressedBlock);

            return suppressedBlock;
        }
    }

    // todo improved this
    private void writeData(byte @NotNull [] data) throws IOException {
        int l = Math.min(getRemaining(), Math.min(data.length, Block.MAX_SIZE));

        synchronized (this) {
            file.write(data, 0, l);
            size += l;
            offset = Math.toIntExact(file.getFilePointer());
        }
    }

    public @Unmodifiable @NotNull Set<@NotNull Block> getBlocks() {
        return Collections.unmodifiableSet(blocks);
    }

    public @Unmodifiable @NotNull Set<@NotNull Block> getSuppressedBlocks() {
        return getBlocks().stream().filter(Block::isSuppressed).collect(Collectors.toSet());
    }

    public boolean hasSuppressed() {
        return suppressed;
    }

    @Override
    public @NotNull Iterator<@NotNull Block> iterator() {
        return blocks.iterator();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull Chunk blocks1 = (Chunk) object;
        return size == blocks1.size && Objects.equals(path, blocks1.path) && Objects.equals(blocks, blocks1.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, blocks, size);
    }

    @SuppressWarnings("warning")
    void deleteFile() throws IOException {
        file.close();
        Files.delete(path);
    }

    // Classes

    public static final class Block {

        private static final int MAX_SIZE = 16 * 1024 * 1024; // 16 MB

        // Objects

        private final @NotNull Chunk chunk;
        private final int index;
        private final int length;
        private final boolean suppressed;

        private Block(@NotNull Chunk chunk, int index, int length, boolean suppressed) {
            if (length > MAX_SIZE) throw new UnsupportedOperationException("Block length exceeded the max size block: " + length);
            this.chunk = chunk;
            this.index = index;
            this.length = length;
            this.suppressed = suppressed;
        }

        // Getters

        public @NotNull Chunk getChunk() {
            return chunk;
        }

        public boolean canWrite(byte @NotNull [] data) {
            return suppressed && (data.length >= this.length);
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

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Block block = (Block) object;
            return index == block.index && length == block.length && suppressed == block.suppressed && Objects.equals(chunk, block.chunk);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, length, suppressed);
        }
    }
}
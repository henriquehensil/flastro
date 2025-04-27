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
                new HashSet<>(512, 0.5f),
                new RandomAccessFile(path.toString(), "rwd"),
                0,
                false
        );
    }

    // Objects

    // Todo synchronized

    private final @NotNull Path path;
    private final @NotNull Set<@NotNull Block> blocks;
    private final @NotNull RandomAccessFile file;

    private int size;
    private volatile boolean suppressed;

    private Chunk(
            @NotNull Path path,
            @NotNull Set<@NotNull Block> blocks,
            @NotNull RandomAccessFile file,
            int size,
            boolean hasSuppressed
    )
            throws IOException
    {
        this.size = size;
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        } else if (!Files.isRegularFile(path)) {
            throw new FileNotFoundException("File must to be a regular file: " + path);
        }

        this.path = path;
        this.blocks = blocks;

        this.file = file;
        this.file.seek(size);

        this.suppressed = hasSuppressed;
    }

    public @NotNull Path getPath() {
        return path;
    }

    public @NotNull Block write(byte @NotNull [] data) throws IOException {
        if (isFull()) {
            throw new FullChunkException("The chunk is already full: " + size);
        }

        int length = Math.min(getRemaining(), Math.min(data.length, Block.MAX_SIZE));
        int start = size;

        write(data, size, length);

        @NotNull Block block = new Block(this, start, length, false);
        blocks.add(block);
        return block;
    }

    public @NotNull Block rewrite(byte @NotNull [] data) throws IOException {
        if (!hasSuppressed()) {
            throw new NoSuchSuppressedBlockException("No suppressed block");
        }

        @NotNull Set<@NotNull Block> suppressedBlocks = getSuppressedBlocks();

        for (@NotNull Block sb : suppressedBlocks) {
            if (sb.canRewrite(data)) {
                write(data, sb.index, sb.length);

                @NotNull Block newBlock = new Block(this, sb.index, sb.length, false);
                blocks.add(newBlock);
                blocks.remove(sb);

                if (suppressedBlocks.size() == 1) {
                    this.suppressed = false;
                }

                return newBlock;
            }
        }

        throw new NoSuchSuppressedBlockException("Cannot find the ideal suppressed block");
    }

    public @NotNull Block delete(@NotNull Block block) {
        if (!has(block)) {
            throw new NoSuchBlockException("Non-member block: " + block);
        } else if (block.isSuppressed()) {
            throw new SuppressedBlockException("This block is already suppressed: " + block);
        } else {
            @NotNull Block suppressedBlock = new Block(this, block.index, block.length, true);

            if (!suppressed) {
                suppressed = true;
            }

            blocks.remove(block);
            blocks.add(suppressedBlock);

            return suppressedBlock;
        }
    }

    private void write(byte @NotNull [] data, int index, int length) throws IOException {
        if (length > Block.MAX_SIZE) {
            throw new IOException("length is greater that block max size");
        } else if (length > (Chunk.MAX_SIZE - index)) {
            throw new IOException("Length is greater that chunk max size");
        } else if (index > size) {
            throw new IOException("Cannot skip positions");
        } else {
            boolean isRandom = index != size;
            if (isRandom) {
                file.seek(index);
            }
            file.write(data, 0, length);
            if (!isRandom) {
                size = (int) file.length();
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean has(@NotNull Block block) {
        return blocks.contains(block);
    }

    public boolean isFull() {
        return size == Chunk.MAX_SIZE;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getRemaining() {
        return Chunk.MAX_SIZE - size;
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

    public static final class Block implements Comparable<@NotNull Block> {

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

        public boolean canRewrite(byte @NotNull [] data) {
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

        @Override
        public int compareTo(@NotNull Block o) {
            return Integer.compare(index, o.index);
        }
    }
}
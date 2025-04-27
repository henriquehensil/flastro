package dev.hensil.flastro.core.storage.file;

import dev.hensil.flastro.core.exception.file.FullChunkException;
import dev.hensil.flastro.core.exception.file.NoSuchSuppressedBlockException;
import dev.hensil.flastro.core.exception.file.SuppressedBlockException;
import dev.hensil.flastro.core.exception.file.NoSuchBlockException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

final class ChuckTests {

    private static final @NotNull Path VAR = Paths.get("").toAbsolutePath().resolve("src/test/files");

    private static int toMegaBytes(int value) {
        return 1024 * 1024 * value;
    }

    // Objects

    private final @NotNull Path path = VAR.resolve("chunk-file-test.dat");
    private final @NotNull Chunk chunk = Chunk.recentChuck(path);

    ChuckTests() throws IOException {
    }

    @AfterEach
    void deleteFiles() throws IOException {
        chunk.deleteFile();
    }

    @Test
    public void path() {
        Assertions.assertEquals(path, chunk.getPath());
        Assertions.assertTrue(Files.exists(chunk.getPath()));
        Assertions.assertTrue(Files.isRegularFile(chunk.getPath()));
    }

    @Test
    @DisplayName("check if the data is on the file") // ruim
    public void data() throws IOException {
        byte @NotNull [] data = "hello".getBytes();
        chunk.write(data);

        Assertions.assertEquals(data.length, Files.size(chunk.getPath()));
        Assertions.assertArrayEquals(data, Files.readAllBytes(chunk.getPath()));
    }

    @Test
    @DisplayName("fails when the chunk reached the max size")
    public void maxSize() throws IOException {
        byte @NotNull [] data = new byte[1024 * 1024]; // 1MB
        Arrays.fill(data, (byte) 0xAB);

        for (short i = 0; i < 512; i++) {
            chunk.write(data);
        }

        Assertions.assertTrue(chunk.isFull());
        Assertions.assertThrows(FullChunkException.class, () -> chunk.write(data));
    }

    @Test
    @DisplayName("chunk size are updated when data is written and removed")
    public void sizeUpdate() throws IOException {
        Assertions.assertTrue(chunk.isEmpty());

        byte @NotNull [] data = "hi".getBytes();
        chunk.write(data);

        Assertions.assertFalse(chunk.isEmpty());
        Assertions.assertEquals(data.length, chunk.size());
    }

    @Test
    public void deleteSuppressedBlock() {
        Assertions.assertThrows(SuppressedBlockException.class, () ->  chunk.delete(chunk.delete(chunk.write("hello".getBytes()))));
    }

    @Test
    public void deleteNonExistentBlock() throws IOException {
        final @NotNull Chunk anotherChunk = Chunk.recentChuck(VAR.resolve("simple-chunk.dat"));

        byte @NotNull [] data = "hi".getBytes();
        @NotNull Chunk.Block nonExistentBlock = anotherChunk.write(data);

        Assertions.assertThrows(NoSuchBlockException.class, () -> this.chunk.delete(nonExistentBlock));

        @NotNull Chunk.Block block = this.chunk.write(data);
        Assertions.assertNotEquals(nonExistentBlock, block);
        Assertions.assertDoesNotThrow(() -> this.chunk.delete(block));

        anotherChunk.deleteFile();
    }

    @Test
    public void suppressedBlocksDoestNotFill() throws IOException {
        byte @NotNull [] data = "hello".getBytes();
        @NotNull Chunk.Block block = chunk.write(data);

        @NotNull Chunk.Block suppressed = chunk.delete(block);

        Assertions.assertFalse(chunk.has(block));
        Assertions.assertTrue(chunk.has(suppressed));
    }

    @Test
    @DisplayName("The block doest not processes the data volume if his length is greater of the max size block")
    public void remainData() throws IOException {
        byte @NotNull [] data = new byte[toMegaBytes(17)]; // 17MB
        Arrays.fill(data, (byte) 0xAB);
        @NotNull Chunk.Block block = chunk.write(data);

        byte @NotNull [] written = Files.readAllBytes(chunk.getPath());
        Assertions.assertFalse(Arrays.equals(data, written));
        Assertions.assertTrue(Arrays.compare(written, data) < 0);

        Assertions.assertTrue(block.length() < data.length);
    }

    @Test
    @DisplayName("Suppressed blocks can be rewrite only when the capacity and the data is equals or greater")
    public void rewriteCapacity() throws IOException {
        byte @NotNull [] data = "hello".getBytes();

        @NotNull Chunk.Block block = chunk.delete(chunk.write(data));
        Assertions.assertEquals(block.length(), data.length);

        data = "hi".getBytes(); // less
        Assertions.assertFalse(block.canRewrite(data));

        try {
            chunk.rewrite(data);
            Assertions.fail("Data is less that suppressed block length");
        } catch (NoSuchSuppressedBlockException ignore) {
            // Yes
        }
    }

    @Test
    @DisplayName("Cannot rewrite when doest has suppressed")
    public void NoSuppressedRewrite() throws IOException {
        byte @NotNull [] data = "hello".getBytes();
        chunk.write(data);
        Assertions.assertThrows(NoSuchSuppressedBlockException.class, () -> chunk.rewrite(data));
    }

    @Test
    public void rewrite() throws IOException {
        byte @NotNull [] data = "hello".getBytes();
        @NotNull Chunk.Block supressedBlock = chunk.delete(chunk.write(data));

        Assertions.assertEquals(data.length, chunk.size());

        Assertions.assertTrue(supressedBlock.isSuppressed());
        Assertions.assertTrue(chunk.hasSuppressed());

        byte @NotNull [] newData = "follow".getBytes();

        @NotNull Chunk.Block block = chunk.rewrite(data);
        Assertions.assertFalse(block.isSuppressed());
        Assertions.assertEquals(block.index(), supressedBlock.index());
        Assertions.assertEquals(block.length(), supressedBlock.length());

        Assertions.assertEquals(data.length, chunk.size());
        Assertions.assertNotEquals(newData.length, chunk.size());
    }

    @Test
    public void rewriteData() throws IOException {
        @NotNull String data = "hello";

        @NotNull Chunk.Block block = chunk.write(data.getBytes());
        chunk.write(data.getBytes());

        byte @NotNull [] read = Files.readAllBytes(chunk.getPath());
        Assertions.assertArrayEquals(read, (data + data).getBytes());

        chunk.delete(block);

        @NotNull String anotherData = "seven";
        chunk.rewrite(anotherData.getBytes());

        read = Files.readAllBytes(chunk.getPath());
        Assertions.assertFalse(Arrays.equals(read, (data + data).getBytes()));

        Assertions.assertArrayEquals(read, (anotherData + data).getBytes());
    }

    // Classes

    @Nested
    final class BlockTests {

        private BlockTests() {
        }

        @Test
        public void simpleBlockStateTest() throws IOException {
            byte @NotNull [] data = "hello".getBytes();

            @NotNull Chunk.Block block = chunk.write("hello".getBytes());

            Assertions.assertEquals(block.length(), data.length);
            Assertions.assertEquals(0, block.index());
            Assertions.assertFalse(block.isSuppressed());

            Assertions.assertTrue(chunk.has(block));
        }

        @Test
        @DisplayName("Blocks indexes are update")
        public void index() throws IOException {
            Assertions.assertTrue(chunk.isEmpty());

            byte @NotNull [] data = "hello".getBytes();
            int offset = 0;

            @NotNull Chunk.Block block = chunk.write("hello".getBytes());
            Assertions.assertEquals(offset, block.index());

            offset += chunk.size();

            block = chunk.write(data);
            Assertions.assertEquals(offset, block.index());
        }

        @Test
        @DisplayName("Blocks max length is 16MB")
        public void maxSize() throws IOException {
            byte @NotNull [] data = new byte[toMegaBytes(17)]; // 17MB
            Arrays.fill(data, (byte) 0xAB);

            @NotNull Chunk.Block block = chunk.write(data);
            Assertions.assertEquals(toMegaBytes(16), block.length());

            Assertions.assertEquals(Files.size(chunk.getPath()), toMegaBytes(16));
        }

        @Test
        @DisplayName("When a block is deleted, a new suppressed block is created")
        public void newSuppressedBlock() throws IOException {
            @NotNull Chunk.Block block = chunk.write("hello".getBytes());
            Assertions.assertFalse(chunk.hasSuppressed());

            @NotNull Chunk.Block suppressed = chunk.delete(block);
            Assertions.assertTrue(chunk.hasSuppressed());
            Assertions.assertNotEquals(block, suppressed);

            Assertions.assertEquals(block.getChunk(), suppressed.getChunk());
            Assertions.assertEquals(block.index(), suppressed.index());
            Assertions.assertEquals(block.length(), suppressed.length());
            Assertions.assertNotEquals(block.isSuppressed(), suppressed.isSuppressed());

            Assertions.assertTrue(suppressed.isSuppressed());
        }

        @Test
        public void canRewrite() throws IOException {
            byte @NotNull [] data = "hello".getBytes();

            @NotNull Chunk.Block block = chunk.write(data);
            Assertions.assertFalse(block.canRewrite(data));

            @NotNull Chunk.Block suppressed = chunk.delete(block);
            Assertions.assertFalse(suppressed.canRewrite("".getBytes())); // less

            Assertions.assertTrue(suppressed.canRewrite("hello world".getBytes())); // more
            Assertions.assertTrue(suppressed.canRewrite(data));
        }
    }
}
package dev.hensil.flastro.core.storage.file;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class ChuckTests {

    private static final @NotNull Path VAR = Paths.get("").toAbsolutePath().resolve("src/test/files");

    // Objects

    private final @NotNull Path path = VAR.resolve("chunk-file-test");
    private final @NotNull Chunk chunk = Chunk.recentChuck(path);

    ChuckTests() throws IOException {
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
    public void maxSize() {

    }

    @Test
    public void deleteSuppressedBlock() {

    }

    @Test
    public void deleteNonExistentBlock() {

    }

    @Test
    @DisplayName("When a block is deleted, a new suppressed block is created")
    public void newSuppressedBlock() {

    }

    @Test
    public void suppressedBlocksDoestNotFill() {

    }

    @Test
    @DisplayName("The block doest not processes the data volume if his length is greater of the max size block")
    public void remainData() {

    }

    @Test
    @DisplayName("Suppressed blocks will be rewrite only when the your capacity and the data is equals or grater")
    public void rewrite() {

    }

    // Classes

    @Nested
    final class BlockTests {

        private final @NotNull Path path = VAR.resolve("chunk.block-file-test");
        private final @NotNull Chunk chunk = Chunk.recentChuck(path);

        BlockTests() throws IOException {
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

            offset += (data.length -1) + 1;

            block = chunk.write(data);
            Assertions.assertEquals(offset, block.index());
        }

    }

    @Nested
    final class BigChuckTest {

    }
}
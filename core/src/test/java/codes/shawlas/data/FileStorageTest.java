package codes.shawlas.data;

import codes.shawlas.data.impl.FileStorage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class FileStorageTest {

    private static final @NotNull FileStorage storage;

    static {
        try {
            storage = new FileStorage(Paths.get(System.getProperty("user.dir"), "/root"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileStorageTest() {
    }

    @Test
    @Order(value = 0)
    public void initialContext() throws IOException {
        final @NotNull Path path = Paths.get("user.dir");

        Assertions.assertFalse(storage.getManager().contains(path.toString()));
        Assertions.assertFalse(storage.getManager().delete(path.toString()));
    }

    @Test
    @Order(value = 1)
    public void createSimple() {
        Assertions.assertDoesNotThrow(() -> storage.getManager().create("", "test"));
    }

    @Test
    @Order(value = 2)
    public void contains() {
        Assertions.assertTrue(storage.getManager().contains("test"));
        Assertions.assertTrue(storage.getManager().contains(storage.getRoot().resolve("test")));
    }

    @Test
    @Order(value = 3)
    public void delete() throws IOException {
        Assertions.assertTrue(storage.getManager().delete("test"));
        Assertions.assertFalse(storage.getManager().contains("test"));
    }

    @Test
    @Order(value = 4)
    public void deleteAll() {
        storage.getManager().deleteAll();
        Assertions.assertFalse(storage.getManager().contains("test"));
    }
}
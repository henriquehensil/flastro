package codes.shawlas.data;

import codes.shawlas.data.impl.FileStorage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class FileStorageTest {

    private final @NotNull FileStorage storage = new FileStorage(Paths.get(System.getProperty("user.dir"), "/root"));

    private FileStorageTest() throws IOException {
    }

    @Test
    @Order(value = 0)
    public void initialContext() throws IOException {
        final @NotNull Path path = Paths.get("user.dir");

        Assertions.assertFalse(storage.getManager().contains(path));
        Assertions.assertFalse(storage.getManager().delete(path));
    }

    @Test
    @Order(value = 1)
    public void createSimple() throws IOException {
        storage.getManager().create(Paths.get(""), "test", new ByteArrayInputStream("hello world".getBytes()));

        Assertions.assertTrue(storage.getManager().contains(Paths.get("","test")));
    }

}

package storage;


import ghostface.dev.impl.file.FileStorageImpl;
import ghostface.dev.storage.file.FileStorage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutionException;

public final class FileStorageTest {

    private final @NotNull FileStorage fileStorage = FileStorageImpl.getInstance();

    @Test
    @DisplayName("Simple test")
    public void test() throws Throwable {
        byte @NotNull [] bytes = "Chupa Corinthians".getBytes();

        Assertions.assertTrue(fileStorage.save("corinthians", new ByteArrayInputStream(bytes)).get());
        Assertions.assertFalse(fileStorage.save("corinthians", new ByteArrayInputStream("ronaldo".getBytes())).get());

        Assertions.assertTrue(fileStorage.get("corinthians").isPresent());

        // Delete all
        Assertions.assertTrue(fileStorage.deleteAll().get());
        Assertions.assertFalse(fileStorage.get("corinthians").isPresent());
    }
}

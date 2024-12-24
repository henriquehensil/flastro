package codes.shawlas.storage;

import codes.shawlas.Context;
import codes.shawlas.Exceptionally;
import codes.shawlas.FieldsProviders;
import codes.shawlas.exception.file.FileAlreadyExistsException;
import codes.shawlas.file.MetaFile;
import codes.shawlas.impl.core.FileStorageImpl;
import codes.shawlas.impl.core.FileStorageImpl.StoragesImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class GeneralFileStorageTests implements Context, Exceptionally {
    private static final @NotNull FileStorageImpl storage = FieldsProviders.getFileStorage();

    @Test
    @Override
    @Order(value = 0)
    public void initialContext() {
        final @NotNull StoragesImpl files = storage.getFiles();

        @NotNull Path path = Paths.get(System.getProperty("user.dir"), "/root");
        Assertions.assertEquals(storage.getDefault(), path);
        Assertions.assertTrue(files.toCollection().isEmpty());

        Assertions.assertFalse(files.iterator().hasNext());
        Assertions.assertFalse(files.get(path).isPresent());
        Assertions.assertFalse(files.get(Paths.get("non/existent/path")).isPresent());

        Assertions.assertFalse(files.delete(path));
        Assertions.assertFalse(files.delete(storage.getDefault()));
    }

    @Test
    @DisplayName("Tests the overall operations")
    @Order(value = 1)
    public void tests() throws Throwable {
        final @NotNull StoragesImpl files = storage.getFiles();

        files.create("tests.json");
        files.create("java/folder", "file.json");
        Assertions.assertFalse(files.toCollection().isEmpty());
        Assertions.assertTrue(files.iterator().hasNext());

        Assertions.assertTrue(files.get(Paths.get("tests.json")).isPresent());
        Assertions.assertTrue(files.get(Paths.get("java/folder", "file.json")).isPresent());

        Assertions.assertTrue(files.get(storage.getDefault().resolve("tests.json")).isPresent());
        Assertions.assertTrue(files.get(storage.getDefault().resolve("java/folder/file.json")).isPresent());

        files.create("tests");
        @NotNull Path tests = Paths.get("tests");

        Assertions.assertTrue(files.delete(tests));
        Assertions.assertFalse(files.get(tests).isPresent());

        files.create("tests");
        Assertions.assertTrue(files.delete(storage.getDefault().resolve(tests)));
        Assertions.assertFalse(files.get(tests).isPresent());

        Assertions.assertEquals(2, files.toCollection().size());
        clearAll(files);

        Assertions.assertTrue(files.delete(Paths.get("java", "folder")));
        Assertions.assertTrue(files.delete(Paths.get("java")));
    }

    private void clearAll(@NotNull StoragesImpl files) {
        for (@NotNull MetaFile file : files.toCollection()) {
            files.delete(file.getPath());
        }
    }

    @Override
    @Test
    @Order(value = 2)
    public void commonExceptions() throws Throwable {
        final @NotNull StoragesImpl files = storage.getFiles();
        // InvalidNameException
        InvalidNamesExceptionally(files);
        InvalidFoldersExceptionally(files);
        InvalidFileExceptionally(files);

        @NotNull Path path = storage.getDefault().resolve("TestIfExists");
        Files.deleteIfExists(path);

        files.create("TestIfExists");
        Assertions.assertTrue(files.get(Paths.get("TestIfExists")).isPresent());
        Assertions.assertTrue(files.get(path).isPresent());

        path = path.getParent().resolve("anFolder\\TestIfExists");
        Files.deleteIfExists(path);

        files.create("anFolder" ,"TestIfExists");
        Assertions.assertTrue(files.get(Paths.get("anFolder/TestIfExists")).isPresent());
        Assertions.assertTrue(files.get(path).isPresent());

        try {
            files.create("TestIfExists");
            Assertions.fail();
        } catch (FileAlreadyExistsException ignore) {
            //
        } try {
            files.create("anFolder", "TestIfExists");
            Assertions.fail();
        } catch (FileAlreadyExistsException ignore) {
            //
        }

        clearAll(files);

        Assertions.assertTrue(files.delete(Paths.get("anFolder")));
    }

    private void InvalidNamesExceptionally(@NotNull StoragesImpl files) throws Throwable {
        try {
            files.create("");
            Assertions.fail("Empty name file");
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("name|file.json");
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("file:test.json");
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("?file");
        } catch (InvalidPathException ignore) {
            //
        }
    }
    private void InvalidFoldersExceptionally(@NotNull StoragesImpl files) throws Throwable {
        try {
            files.create("", "");
            Assertions.fail("folder and name cannot be empty");
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("name|folders", "file");
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
             files.create("?folder", "file.json");
             Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("folder:test", "tests");
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.create("folder\"", "tests");
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        }
    }
    private void InvalidFileExceptionally(@NotNull StoragesImpl files) throws Throwable {
        try {
            files.store(Paths.get("folder", "tes?ts").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.store(Paths.get("folder:test", "tests").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.store(Paths.get("folder\"", "tests").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.store(Paths.get("tests", "?file").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.store(Paths.get("tests", "file:").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        } try {
            files.store(Paths.get("tests", "file|").toFile());
            Assertions.fail();
        } catch (InvalidPathException ignore) {
            //
        }
    }
}

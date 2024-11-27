package table;

import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExistsException;
import ghostface.dev.exception.column.ColumnException;
import ghostface.dev.exception.key.DuplicatedKeyValueException;
import ghostface.dev.exception.key.MissingKeyException;
import ghostface.dev.exception.table.TableStateException;
import ghostface.dev.impl.database.AuthenticationImpl;
import ghostface.dev.impl.database.DatabaseImpl;
import ghostface.dev.impl.table.KeyImpl;
import ghostface.dev.impl.table.TableImpl;
import ghostface.dev.impl.table.ColumnsImpl;
import ghostface.dev.impl.table.ElementsImpl;
import ghostface.dev.table.Key;
import ghostface.dev.table.column.Column;
import ghostface.dev.table.data.Data;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.UUID;

public final class TableEntitiesTest {
    private static final @NotNull DatabaseImpl database = new DatabaseImpl(new AuthenticationImpl(new InetSocketAddress(80), "user", "database"));
    private final @NotNull TableImpl table = new TableImpl(database);

    @Test
    @DisplayName("Tests the initial context of the Elements")
    public void initialElements() {
        @NotNull ElementsImpl elements = table.getElements();

        Assertions.assertFalse(elements.get(1).isPresent());
        Assertions.assertFalse(elements.remove(1));
        Assertions.assertTrue(elements.toCollection().isEmpty());

        try {
            elements.create();
        } catch (TableStateException e) {
            Assertions.assertTrue(true);
        } catch (DuplicatedKeyValueException | MissingKeyException e) {
            Assertions.fail("That can't never happen");
        }
    }

    @Test
    @DisplayName("Tests the initial context of the Elements")
    public void initialColumns() {
        @NotNull ColumnsImpl columns = table.getColumns();

        Assertions.assertFalse(columns.contains("unknown"));

        Assertions.assertEquals(0, columns.size());
        Assertions.assertEquals(0, columns.getKeys().size());
        Assertions.assertEquals(0, columns.getWithoutKeys().size());
        Assertions.assertEquals(0, columns.toCollection().size());
    }

    @Test
    @DisplayName("Elements when has columns")
    public void ElementsWithColumn() {
        @NotNull ColumnsImpl columns = table.getColumns();
        @NotNull ElementsImpl elements = table.getElements();

        columns.create("ageTest", DataType.INTEGER, 10, false);

        Assertions.assertEquals(1, columns.size());
        Assertions.assertTrue(columns.getKeys().isEmpty());
        Assertions.assertTrue(columns.contains("ageTest"));
        Assertions.assertTrue(columns.get("ageTest", DataType.INTEGER).isPresent());

        try {
            elements.create();
        } catch (Throwable e) {
            Assertions.fail("WTF");
        }

        Assertions.assertFalse(elements.toCollection().isEmpty());
        Assertions.assertTrue(elements.get(1).isPresent());

        @NotNull Data data = elements.get(1).get();

        Assertions.assertEquals(10, data.get(columns.get("ageTest", DataType.INTEGER).get()));
    }

    @Test
    @DisplayName("Test invalids operation")
    public void invalidsTests() throws Throwable {
        @NotNull ColumnsImpl columns = new TableImpl(database).getColumns();
        @NotNull ElementsImpl elements = columns.getTable().getElements();

        // if no has column
        try {
            elements.create();
            Assertions.fail("Columns does not exists");
        } catch (TableStateException ignore) {
            //
        }

        columns.create("test", DataType.STRING, "test value",false);

        // If name already exists
        try {
            columns.create("test", DataType.INTEGER, 1, true);
            Assertions.fail("Name already exists");
        } catch (NameAlreadyExistsException ignore) {
            //
        }

        // if create key column when has elements
        elements.create();
        Assertions.assertTrue(elements.get(1).isPresent());
        try {
            columns.createKey("keyTest", DataType.BOOLEAN);
            Assertions.fail();
        } catch (TableStateException ignore) {
            //
        }

        Assertions.assertTrue(elements.toCollection().contains(elements.get(1).get()));
        Assertions.assertTrue(elements.get(1).get().toCollection().contains("test value"));

        // added new column and load them
        columns.create("helloColumn", DataType.STRING, "hello", false);
        columns.create("worldColumn", DataType.STRING, "world", true);
        Assertions.assertTrue(columns.get("helloColumn", DataType.STRING).isPresent());
        Assertions.assertTrue(columns.get("worldColumn", DataType.STRING).isPresent());

        Assertions.assertFalse(elements.get(1).get().toCollection().contains("hello"));
        Assertions.assertFalse(elements.get(1).get().toCollection().contains("world"));
        elements.load(); // load the new columns
        Assertions.assertTrue(elements.get(1).get().toCollection().contains("hello"));
        Assertions.assertTrue(elements.get(1).get().toCollection().contains("world"));

        /*
        * invalids operations:
        * Set an value
        * */
        try {
            elements.get(1).get().set(columns.get("helloColumn", DataType.STRING).get(), null);
            Assertions.fail("Column is not nullable");
        } catch (ColumnException ignore) {
            //
        }

        // Todo: set by an no-existent column

        // Set value

        @NotNull Column<String> column = columns.get("helloColumn", DataType.STRING).get();

        elements.get(1).get().set(column, "java");

        Assertions.assertNotEquals("hello", elements.get(1).get().get(column));
        Assertions.assertFalse(elements.get(1).get().toCollection().contains("hello"));

        Assertions.assertEquals("java", elements.get(1).get().get(column));
        Assertions.assertTrue(elements.get(1).get().toCollection().contains("java"));
    }

    @Test
    @DisplayName("Tests keys")
    public void keysTest() throws Throwable {
        @NotNull Column<String> uuidColumn = table.getColumns().createKey("UUID", DataType.STRING);
        @NotNull Column<String> emailColumn = table.getColumns().createKey("email", DataType.STRING);
        table.getColumns().create("test", DataType.STRING, "000", false);

        Assertions.assertTrue(table.getColumns().get("UUID", DataType.STRING).isPresent());
        Assertions.assertTrue(table.getColumns().get("email", DataType.STRING).isPresent());
        Assertions.assertTrue(table.getColumns().get("test", DataType.STRING).isPresent());

        Assertions.assertTrue(table.getColumns().get("UUID", DataType.STRING).get().isKey());
        Assertions.assertTrue(table.getColumns().get("email", DataType.STRING).get().isKey());
        Assertions.assertFalse(table.getColumns().get("test", DataType.STRING).get().isKey());

        Assertions.assertTrue(table.getColumns().contains("UUID"));
        Assertions.assertTrue(table.getColumns().contains("email"));
        Assertions.assertTrue(table.getColumns().contains("test"));

        // created elements without key
        try {
            table.getElements().create();
            Assertions.fail("Has key");
        } catch (MissingKeyException ignore) {
            //
        }

        // Create keys

        @NotNull UUID uuid = UUID.randomUUID();
        @NotNull Key<String> uuidKey = new KeyImpl<>(uuidColumn, uuid.toString());
        @NotNull Key<String> emailKey = new KeyImpl<>(emailColumn, "test@email");
        table.getElements().create(uuidKey, emailKey);

        // Duplicated Key Value
        try {
            table.getElements().create(emailKey, uuidKey);
            Assertions.fail();
        } catch (DuplicatedKeyValueException ignore) {
            //
        }

        Assertions.assertTrue(table.getElements().get(1).isPresent());
        Assertions.assertTrue(table.getElements().get(1).get().toCollection().contains("test@email"));
        Assertions.assertTrue(table.getElements().get(1).get().toCollection().contains(uuid.toString()));
        Assertions.assertTrue(table.getElements().get(1).get().toCollection().contains("000"));
    }
}
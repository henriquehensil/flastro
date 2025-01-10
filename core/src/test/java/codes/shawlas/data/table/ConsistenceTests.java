package codes.shawlas.data.table;

import codes.shawlas.data.table.Table.*;
import codes.shawlas.data.DataType;
import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class ConsistenceTests {

    private final @NotNull Elements elements;
    private final @NotNull Columns columns;

    private ConsistenceTests() throws TableAlreadyExistsException {
        @NotNull TableProvider provider = new TableProvider("Test");
        this.elements = provider.getElements();
        this.columns = provider.getColumns();
    }

    @Test
    @DisplayName("Elements upgrade by the column operations")
    public void test() throws Throwable {
        @NotNull Column<?> column = columns.create("test-column", DataType.STRING, "value", true);

        @NotNull Element element = elements.create();
        Assertions.assertTrue(element.containsValue("value"));
        Assertions.assertEquals("value", element.getValue(column));
        Assertions.assertEquals(1, element.getData().size());

        Assertions.assertFalse(element.containsValue("undefined"));

        column = columns.create("name", DataType.STRING, "undefined", true);

        Assertions.assertTrue(element.containsValue("undefined"));
        Assertions.assertEquals("undefined", element.getValue(column));
        Assertions.assertEquals(2, element.getData().size());

        columns.delete("test-column");

        Assertions.assertFalse(element.containsValue("value"));
        Assertions.assertEquals(1, element.getData().size());
    }

    @Test
    @DisplayName("Tests the EntryData consistence of element")
    public void entryDataConsistence() throws Throwable {
        @NotNull Column<?> column = columns.create("test-column", DataType.STRING, "value", true);

        @NotNull Element element = elements.create();
        Assertions.assertFalse(element.getData().isEmpty());
        Assertions.assertEquals(1, element.getData().size());

        @Nullable EntryData<?> data = element.getData().stream().filter(entry -> entry.getColumn().equals(column)).findFirst().orElse(null);
        Assertions.assertNotNull(data);

        @NotNull Column<?> column2 = columns.create("name", DataType.STRING, "undefined", true);
        Assertions.assertEquals(2, element.getData().size());
        Assertions.assertNotNull(element.getData().stream().filter(entry -> entry.getColumn().equals(column2)).findFirst().orElse(null));

        columns.delete("test-column");
        Assertions.assertFalse(element.getData().contains(data));
        Assertions.assertEquals(1, element.getData().size());

        for (int i = 1; i < 6; i++) {
            columns.create("col-" + i, DataType.BOOLEAN, false, false);
            Assertions.assertEquals(i + 1, element.getData().size());
        }

        for (int i = 5; i > 0; i--) {
            columns.delete("col-" + i);
            Assertions.assertEquals(i, element.getData().size());
        }
    }

    @Test
    @DisplayName("Tests the column comparators")
    public void columnsComparators() throws Throwable {
        @NotNull Column<?> columnKey1 = columns.create("Amazing", DataType.BOOLEAN);
        @NotNull Column<?> columnKey2 = columns.create("Zebra", DataType.INTEGER);

        // ColumnKey 1 must be before that columnKey 2
        Assertions.assertTrue(columnKey1.compareTo(columnKey2) < 0);
        Assertions.assertTrue(columnKey2.compareTo(columnKey1) > 0);

        @NotNull Column<?> nullable1 = columns.create("Armor", DataType.STRING, null, true);
        @NotNull Column<?> nullable2 = columns.create("Life", DataType.STRING, null, true);

        // Nullable 1 must be before that Nullable 2
        Assertions.assertTrue(nullable1.compareTo(nullable2) < 0);
        Assertions.assertTrue(nullable2.compareTo(nullable1) > 0);

        @NotNull Column<?> normal = columns.create("Normal", DataType.STRING, "just a quiet guy", false);

        // normal must be before that nullables and later that keys
        Assertions.assertTrue(normal.compareTo(nullable1) < 0);
        Assertions.assertTrue(normal.compareTo(nullable2) < 0);

        Assertions.assertTrue(normal.compareTo(columnKey1) > 0);
        Assertions.assertTrue(normal.compareTo(columnKey2) > 0);
    }

    @Test
    @DisplayName("Elements rows are updatable")
    public void elementsRows() throws Throwable {
        columns.create("test-column", DataType.STRING, "value", true);

        @NotNull Element element1 = elements.create();
        Assertions.assertEquals(1, element1.getIndex());
        element1 = elements.create(); // index 2
        Assertions.assertEquals(2, element1.getIndex());

        Assertions.assertTrue(elements.get(1).isPresent());
        Assertions.assertTrue(elements.get(2).isPresent());

        elements.delete(1);
        Assertions.assertEquals(1, element1.getIndex());
        Assertions.assertTrue(elements.get(1).isPresent());
        Assertions.assertEquals(elements.get(1).get(), element1);

        @NotNull Element element2 = elements.create();
        @NotNull Element element3 = elements.create();
        @NotNull Element element4 = elements.create();
        Assertions.assertTrue(elements.get(2).isPresent());
        Assertions.assertTrue(elements.get(3).isPresent());
        Assertions.assertTrue(elements.get(4).isPresent());

        Assertions.assertEquals(1, element1.getIndex());
        Assertions.assertEquals(2, element2.getIndex());
        Assertions.assertEquals(3, element3.getIndex());
        Assertions.assertEquals(4, element4.getIndex());

        elements.delete(1);
        Assertions.assertEquals(1, element2.getIndex());
        Assertions.assertEquals(2, element3.getIndex());
        Assertions.assertEquals(3, element4.getIndex());

        elements.delete(2);
        Assertions.assertEquals(1, element2.getIndex());
        Assertions.assertEquals(2, element4.getIndex());

        elements.delete(1);
        Assertions.assertEquals(1, element4.getIndex());

        Assertions.assertEquals(1, elements.getAll().size());
    }
}
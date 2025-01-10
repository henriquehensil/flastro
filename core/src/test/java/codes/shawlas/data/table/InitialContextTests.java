package codes.shawlas.data.table;

import codes.shawlas.data.exception.column.NoColumnsException;
import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import codes.shawlas.data.table.Table.Columns;
import codes.shawlas.data.table.Table.Elements;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class InitialContextTests {

    private final @NotNull Columns columns;
    private final @NotNull Elements elements;

    public InitialContextTests() throws TableAlreadyExistsException {
        @NotNull Table table = new TableProvider("Tests").getTable();
        this.columns = table.getColumns();
        this.elements = table.getElements();
    }

    @Test
    @DisplayName("Test the initial context of elements")
    public void elements() {
        Assertions.assertTrue(elements.getAll().isEmpty());
        Assertions.assertFalse(elements.get(1).isPresent());
        Assertions.assertFalse(elements.delete(1));
        Assertions.assertThrows(NoColumnsException.class, elements::create);
    }

    @Test
    @DisplayName("Test the initial context of columns")
    public void columns() throws Throwable {
        Assertions.assertTrue(columns.getAll().isEmpty());
        Assertions.assertTrue(columns.getKeys().isEmpty());
        Assertions.assertFalse(columns.get("NonexistentColumn").isPresent());
        Assertions.assertFalse(columns.delete("nonexistentColumn"));
    }
}

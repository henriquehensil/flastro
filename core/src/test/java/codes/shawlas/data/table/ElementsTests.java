package codes.shawlas.data.table;

import codes.shawlas.data.DataType;
import codes.shawlas.data.exception.column.*;
import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import codes.shawlas.data.table.Table.*;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class ElementsTests {

    private final @NotNull Elements elements;

    public ElementsTests() throws TableAlreadyExistsException {
        this.elements = new TableProvider("Tests").getElements();
    }

    @Test
    @DisplayName("Cannot create new elements if it doesn't exist any column")
    public void columnsNotExists() {
        Assertions.assertThrows(NoColumnsException.class, elements::create);
    }

    @Test
    @DisplayName("You cannot forgot no one key")
    public void elementsWithoutKey() throws Throwable {
        @NotNull Column<@NotNull Integer> id = elements.getTable().getColumns().create("id", DataType.INTEGER);
        @NotNull Column<@NotNull String> email = elements.getTable().getColumns().create("email", DataType.STRING);
        @NotNull Column<@NotNull String> cpf = elements.getTable().getColumns().create("cpf", DataType.STRING);

        Assertions.assertThrows(MissingKeyColumnException.class, elements::create);

        @NotNull EntryData<?> dataId = new EntryData<>(id, 1);
        Assertions.assertThrows(MissingKeyColumnException.class, () -> elements.create(dataId));

        @NotNull EntryData<?> dataEmail = new EntryData<>(email, "shawlas@gmail.com");
        Assertions.assertThrows(MissingKeyColumnException.class, () -> elements.create(dataId, dataEmail));

        @NotNull EntryData<?> dataCpf = new EntryData<>(cpf, "000.000.000-00");
        Assertions.assertDoesNotThrow(() -> elements.create(dataId, dataEmail, dataCpf));
    }

    @Test
    @DisplayName("Test if has duplicated columns")
    public void duplicatedColumn() throws Exception {
        @NotNull Column<@NotNull Integer> id = elements.getTable().getColumns().create("id", DataType.INTEGER);
        @NotNull Column<@NotNull Boolean> verified = elements.getTable().getColumns().create("verified", DataType.BOOLEAN, false, false);

        @NotNull EntryData<?> dataId = new EntryData<>(id, 1);
        @NotNull EntryData<?> dataVerified = new EntryData<>(verified, true);
        Assertions.assertThrows(DuplicatedColumnException.class, () -> elements.create(dataId, dataVerified, dataVerified));
        Assertions.assertThrows(DuplicatedColumnException.class, () -> elements.create(dataId, dataId, dataVerified));
    }

    @Test
    @DisplayName("Test with invalid Column")
    public void invalidColumn() throws Throwable {
        @NotNull Column<@NotNull String> email = elements.getTable().getColumns().create("email", DataType.STRING);
        @NotNull Column<@NotNull String> cpf = elements.getTable().getColumns().create("cpf", DataType.STRING);
        @NotNull Column<@NotNull Boolean> verified = elements.getTable().getColumns().create("verified", DataType.BOOLEAN, false, false);

        @NotNull Column<@NotNull Integer> invalid = elements.getTable().getColumns().create("id", DataType.INTEGER);
        Assertions.assertTrue(elements.getTable().getColumns().delete("id"));

        @NotNull EntryData<?> dataEmail = new EntryData<>(email, "shawlas@gmail.com");
        @NotNull EntryData<?> dataCpf = new EntryData<>(cpf, "000.000.000-00");
        @NotNull EntryData<?> dataVerified = new EntryData<>(verified, true);

        @NotNull EntryData<?> dataInvalid = new EntryData<>(invalid, 4);

        Assertions.assertThrows(InvalidColumnException.class, () -> elements.create(dataEmail, dataCpf, dataVerified, dataInvalid));

        Assertions.assertDoesNotThrow(() -> elements.create(dataEmail, dataCpf, dataVerified));
    }

    @Test
    @DisplayName("Table cannot accept duplicated key column values")
    public void duplicatedValues() throws Throwable {
        final @NotNull Column<@NotNull String> email = elements.getTable().getColumns().create("email", DataType.STRING);
        final @NotNull Column<@NotNull Boolean> verified = elements.getTable().getColumns().create("verified", DataType.BOOLEAN, false, false);

        @NotNull EntryData<?> dataEmail = new EntryData<>(email, "shawlas@gmail.com");
        @NotNull EntryData<?> dataVerified = new EntryData<>(verified, true);

        elements.create(dataEmail, dataVerified);

        dataEmail = new EntryData<>(email, "anotherShawlas@gmail.com");
        elements.create(dataEmail, dataVerified);

        try {
            dataEmail = new EntryData<>(email, "shawlas@gmail.com");
            elements.create(dataEmail, dataVerified);
            Assertions.fail("Cannot accept duplicated value");
        } catch (DuplicatedKeyValueException ignore) {
            //
        }
    }
}
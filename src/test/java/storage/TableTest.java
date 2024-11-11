package storage;

import ghostface.dev.exception.table.TableException;
import ghostface.dev.impl.table.DataImpl;
import ghostface.dev.impl.table.TableImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public final class TableTest {

    @Test
    public void test() throws TableException {
        @NotNull TableImpl table = new TableImpl("Brasil");
        @NotNull TableImpl table1 = new TableImpl("Brasil");

        Assertions.assertEquals(table ,table1);

        table.add(new DataImpl(UUID.randomUUID().toString(), table));

        Assertions.assertNotEquals(table ,table1);
    }

}

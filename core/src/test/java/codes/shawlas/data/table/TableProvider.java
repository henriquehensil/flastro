package codes.shawlas.data.table;

import codes.shawlas.data.Database;
import codes.shawlas.data.exception.table.TableAlreadyExistsException;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

final class TableProvider {

    private final @NotNull Table table;

    TableProvider(@NotNull String tableName) throws TableAlreadyExistsException {
        @NotNull Database.Authentication auth = Database.Authentication.create(new InetSocketAddress(80), "", "");
        @NotNull Database database = Database.create(auth);
        this.table = database.getTableStorage().getTables().create(tableName);
    }

    // Getters

    @NotNull Table getTable() {
        return table;
    }

    @NotNull Table.Elements getElements() {
        return table.getElements();
    }

    @NotNull Table.Columns getColumns() {
        return table.getColumns();
    }
}

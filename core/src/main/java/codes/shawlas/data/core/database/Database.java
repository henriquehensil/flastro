package codes.shawlas.data.core.database;

import codes.shawlas.data.core.file.FileStorage;
import codes.shawlas.data.core.nest.NestStorage;
import codes.shawlas.data.core.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    @NotNull TableStorage getTableStorage();
}

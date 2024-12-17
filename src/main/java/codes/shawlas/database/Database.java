package codes.shawlas.database;

import codes.shawlas.file.FileStorage;
import codes.shawlas.nest.NestStorage;
import codes.shawlas.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    @NotNull TableStorage getTableStorage();
}

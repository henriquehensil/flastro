package codes.shawlas.data.connection;

import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.nest.NestStorage;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

}
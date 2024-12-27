package codes.shawlas.jdata.core.database;

import codes.shawlas.jdata.core.file.FileStorage;
import codes.shawlas.jdata.core.nest.NestStorage;
import codes.shawlas.jdata.core.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    @NotNull TableStorage getTableStorage();
}

package ghostface.dev.database;

import ghostface.dev.file.FileStorage;
import ghostface.dev.nest.NestStorage;
import ghostface.dev.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    @NotNull TableStorage getTableStorage();
}

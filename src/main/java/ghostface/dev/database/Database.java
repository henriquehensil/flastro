package ghostface.dev.database;

import ghostface.dev.storage.nest.NestStorage;
import ghostface.dev.storage.file.FileStorage;
import ghostface.dev.storage.table.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getBoxStorage();

}

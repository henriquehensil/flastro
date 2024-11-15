package ghostface.dev.database;

import ghostface.dev.storage.FileStorage;
import ghostface.dev.storage.NestStorage;
import ghostface.dev.storage.TableStorage;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    @NotNull TableStorage getTableStorage();
}

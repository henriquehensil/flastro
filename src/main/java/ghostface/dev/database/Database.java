package ghostface.dev.database;

import ghostface.dev.storage.FilesStorages;
import ghostface.dev.table.Tables;
import org.jetbrains.annotations.NotNull;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull Tables getTables();

    @NotNull FilesStorages getStorages();

}

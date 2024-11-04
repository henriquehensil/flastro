package ghostface.dev.storage;

import ghostface.dev.exception.DataException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface FileStorage {

    @NotNull File get() throws DataException;

    boolean contains();

}

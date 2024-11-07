package ghostface.dev.datatype;

import ghostface.dev.exception.DataException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface DataType<T> {

    @NotNull T read(@NotNull InputStream stream) throws IOException, DataException;

    @NotNull T read(byte @NotNull [] bytes) throws DataException;

}

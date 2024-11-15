package ghostface.dev;

import ghostface.dev.exception.DataTypeException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;

public interface DataType<T> {

    /**
     * @throws DataTypeException if data is not a valid DataType
     * */
    @NotNull T read(@NotNull InputStream stream) throws DataTypeException, IOException;

    @NotNull Class<T> getType();
}

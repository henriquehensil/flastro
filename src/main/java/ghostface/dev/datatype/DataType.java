package ghostface.dev.datatype;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface DataType<T> {

    @NotNull T read(@NotNull InputStream stream) throws IOException;

    @NotNull T read(byte @NotNull [] bytes);

}

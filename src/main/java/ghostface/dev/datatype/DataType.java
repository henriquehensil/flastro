package ghostface.dev.datatype;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface DataType {

    @NotNull Object read(@NotNull InputStream stream) throws IOException;

    @NotNull Object read(byte @NotNull [] bytes);

}

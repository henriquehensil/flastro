package ghostface.dev.datatype;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface ConcreteType<T> extends DataType {

    @Override
    @NotNull T read(@NotNull InputStream stream) throws IOException;

    @Override
    @NotNull T read(byte @NotNull [] bytes);
}

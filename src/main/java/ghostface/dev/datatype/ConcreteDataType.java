package ghostface.dev.datatype;

import ghostface.dev.exception.DataTypeParserException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface ConcreteDataType<T> extends DataType {

    @Override
    @NotNull T read(@NotNull InputStream stream) throws DataTypeParserException, IOException;

    @Override
    @NotNull T read(byte @NotNull [] bytes) throws DataTypeParserException;
}

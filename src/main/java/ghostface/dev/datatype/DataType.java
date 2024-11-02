package ghostface.dev.datatype;

import ghostface.dev.exception.DataTypeParserException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface DataType {

    @NotNull Object read(@NotNull InputStream stream) throws DataTypeParserException, IOException;

    @NotNull Object read(byte @NotNull [] bytes) throws DataTypeParserException;
}

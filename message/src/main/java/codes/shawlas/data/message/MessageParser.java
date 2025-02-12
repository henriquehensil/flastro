package codes.shawlas.data.message;

import codes.shawlas.data.exception.MessageParserException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface MessageParser {

    @NotNull Message deserialize(@NotNull InputStream inputStream) throws MessageParserException;

}
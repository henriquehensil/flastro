package codes.shawlas.data;

import codes.shawlas.data.exception.MessageParserException;
import org.jetbrains.annotations.NotNull;

public interface MessageParser {

    @NotNull Message.Input deserialize(byte @NotNull [] bytes) throws MessageParserException;

}
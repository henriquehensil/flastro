package codes.shawlas.data.exception;

import org.jetbrains.annotations.NotNull;

public class MessageParserException extends Exception {
    public MessageParserException(@NotNull String message) {
        super(message);
    }

    public MessageParserException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}

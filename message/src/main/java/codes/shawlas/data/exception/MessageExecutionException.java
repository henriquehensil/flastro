package codes.shawlas.data.exception;

import org.jetbrains.annotations.NotNull;

public class MessageExecutionException extends Exception {
    public MessageExecutionException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}

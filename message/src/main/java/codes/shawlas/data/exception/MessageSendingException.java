package codes.shawlas.data.exception;

import org.jetbrains.annotations.NotNull;

public class MessageSendingException extends Exception {
    public MessageSendingException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
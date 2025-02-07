package codes.shawlas.data.exception;

import codes.shawlas.data.Message;
import org.jetbrains.annotations.NotNull;

public class NoSuchHeaderException extends RuntimeException {
    public NoSuchHeaderException(@NotNull String attribute, @NotNull Message message) {
        super("The '" + attribute + "' doest not present on the message: " + message);
    }

    public NoSuchHeaderException(@NotNull String message) {
        super(message);
    }
}

package codes.shawlas.data;

import codes.shawlas.data.exception.NoSuchHeaderException;
import org.jetbrains.annotations.NotNull;

public sealed interface Message permits Message.Input, Message.Output {

    @NotNull Object getId();

    /**
     * @throws NoSuchHeaderException if the {@code attribute} is not present
     * */
    @NotNull Object get(@NotNull String attribute) throws NoSuchHeaderException;

    @NotNull Object serialize();

    // Classes

    non-sealed interface Input extends Message {

    }

    non-sealed interface Output extends Message {

    }
}
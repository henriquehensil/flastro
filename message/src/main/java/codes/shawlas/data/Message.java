package codes.shawlas.data;

import codes.shawlas.data.exception.MessageExecutionException;
import codes.shawlas.data.exception.MessageSendingException;
import codes.shawlas.data.exception.NoSuchHeaderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public sealed interface Message permits AbstractMessage, Message.Input, Message.Output {

    @NotNull String getId();

    @Nullable String getCorrelationId();

    @UnknownNullability Object get(@NotNull String attribute) throws NoSuchHeaderException;

    byte @NotNull [] serialize();

    boolean isFinished();

    // Classes

    non-sealed interface Input extends Message {

        void execute() throws MessageExecutionException;

    }

    non-sealed interface Output extends Message {

        void send(boolean finish) throws MessageSendingException;

    }
}
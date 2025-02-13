package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public sealed interface Request extends Message permits Request.Input, Request.Output {

    @NotNull String getCommand();

    boolean isFinished();

    // Classes

    non-sealed interface Input extends Request, Message.Input {

        @Override
        @NotNull String getId();

        @Override
        @NotNull Message.Parser getParser();

    }

    non-sealed interface Output extends Request, Message.Output {

        @Override
        @NotNull String getId();

        @Override
        @NotNull InputStream serialize();

    }
}
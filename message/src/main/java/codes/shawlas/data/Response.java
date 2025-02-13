package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;

public sealed interface Response extends Message permits Response.Input, Response.Output {

    @NotNull Request getRequest();

    @NotNull String getCorrelationId();

    // classes

    non-sealed interface Input extends Response, Message.Input {
    }

    non-sealed interface Output extends Response, Message.Output {
    }
}
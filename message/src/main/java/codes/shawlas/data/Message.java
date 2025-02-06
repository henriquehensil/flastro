package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

public sealed interface Message permits Message.Input, Message.Output {

    @Nullable Request getRequest();

    @NotNull Object getBody();

    // Classes

    non-sealed interface Input extends Message {

        @NotNull Object execute();

    }

    non-sealed interface Output extends Message {

        @NotNull Object getStatus();

        void write(@NotNull OutputStream output) throws IOException;

        void write(@NotNull WritableByteChannel output) throws IOException;

    }
}

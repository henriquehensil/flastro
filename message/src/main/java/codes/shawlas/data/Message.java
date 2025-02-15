package codes.shawlas.data;

import codes.shawlas.data.exception.MessageExecutionException;
import codes.shawlas.data.exception.MessageParserException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public sealed interface Message permits Message.Input, Message.Output {

    @NotNull String getId();

    // Classes

    non-sealed interface Input extends Message {

        @NotNull Message.Parser getParser();

        /**
         * @throws MessageExecutionException if an error occurs while execute the response ({@linkplain Input##optional-restrictions optional})
         * */
        void execute(@NotNull Object @NotNull ... args);

    }

    non-sealed interface Output extends Message {

        @NotNull InputStream serialize();

    }

    interface Parser {

        @NotNull Message.Input deserialize(@NotNull InputStream inputStream) throws MessageParserException;

    }
}
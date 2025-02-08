package codes.shawlas.data;

import codes.shawlas.data.exception.MessageExecutionException;
import codes.shawlas.data.exception.MessageSendingException;
import org.jetbrains.annotations.NotNull;

public interface Response {

    @NotNull Object getId();

    @NotNull Request getRequest();

    @NotNull Message getMessage();

    // Classes

    interface Input extends Response {

        @Override
        @NotNull Message.Input getMessage();

        void execute(@NotNull Object @NotNull ... args) throws MessageExecutionException;;

    }

    interface Output extends Response {

        @Override
        @NotNull Message.Output getMessage();

        void send(boolean finish, @NotNull Object @NotNull ... args) throws MessageSendingException;

    }

}
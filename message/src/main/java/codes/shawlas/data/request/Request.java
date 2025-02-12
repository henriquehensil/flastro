package codes.shawlas.data.request;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

public interface Request extends Message {

    @NotNull String getCommand();

    boolean isFinished();

}
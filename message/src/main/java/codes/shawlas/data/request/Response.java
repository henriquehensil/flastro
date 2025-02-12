package codes.shawlas.data.request;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

public interface Response extends Message {

    @NotNull Request getRequest();

    @NotNull String getCorrelationId();

}
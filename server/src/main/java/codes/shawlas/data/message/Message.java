package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.UUID;

public sealed interface Message permits Message.Input, Message.Output {

    byte getCode();

    @NotNull UUID getId();

    @NotNull OffsetDateTime getTime();

    // Classes

    non-sealed interface Input extends Message {

        default @NotNull MessageExecutor getExecutor(@NotNull Database database) {
            return MessageExecutor.getInstance(this, database);
        }

    }

    non-sealed interface Output extends Message {

        @NotNull ByteBuffer serialize();

    }
}

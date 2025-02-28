package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.time.OffsetDateTime;
import java.util.UUID;

public sealed interface Message permits Message.Input, Message.Output {

    @NotNull UUID getId();

    @NotNull OffsetDateTime getTime();

    byte getCode();

    // Classes

    non-sealed interface Input extends Message {

        /**
         * Execute an operation represented by this message.
         *
         * @param database a database for perform operations.
         * @param buffer a buffer used to query data.
         * @param channel a readable channel to update the buffer if needed.
         * */
        void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel);
    }

    non-sealed interface Output extends Message {

        /**
         * Transform this message in a buffer object
         * */
        @NotNull ByteBuffer serialize();
    }
}
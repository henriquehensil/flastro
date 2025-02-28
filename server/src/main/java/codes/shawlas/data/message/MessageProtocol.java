package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.time.OffsetDateTime;
import java.util.UUID;

public sealed interface MessageProtocol {

    @NotNull String getContent();

    @NotNull Message.Input getContext();

    // Classes

    final class SuccessMessage implements MessageProtocol, Message.Input, Message.Output {

        private final @NotNull UUID id;
        private final @NotNull OffsetDateTime time;
        private final @NotNull String content;
        private final @NotNull Message.Input context;

        public SuccessMessage(
                @NotNull UUID id,
                @NotNull OffsetDateTime time,
                @NotNull String content,
                @NotNull Message.Input context
        ) {
            this.id = id;
            this.time = time;
            this.content = content;
            this.context = context;
        }

        public SuccessMessage(
                @NotNull UUID id,
                @NotNull String content,
                @NotNull Message.Input context
        ) {
            this.id = id;
            this.time = OffsetDateTime.now();
            this.content = content;
            this.context = context;
        }

        @Override
        public @NotNull String getContent() {
            return content;
        }

        @Override
        public @NotNull Message.Input getContext() {
            return context;
        }

        @Override
        public void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel) {

        }

        @Override
        public @NotNull ByteBuffer serialize() {
            int size = id.toString().length() + time.toString().length() + content.length() + context.getId().toString().length();

            @NotNull ByteBuffer buffer = ByteBuffer.allocate(size);


        }

        @Override
        public @NotNull UUID getId() {
            return id;
        }

        @Override
        public @NotNull OffsetDateTime getTime() {
            return time;
        }

        @Override
        public byte getCode() {
            return 0x1;
        }
    }

    final class ExceptionMessage implements MessageProtocol {

        @Override
        public @NotNull String getContent() {
            return "";
        }

        @Override
        public @NotNull Message.Input getContext() {
            return null;
        }
    }
}
package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.message.MessageExecutorException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class MessageExecutor {

    // Static

    public static @NotNull MessageExecutor getInstance(@NotNull Message.Input message, @NotNull Database database) {
        return MessageExecutors.get(message, database);
    }

    // Objects

    protected final @NotNull Message.Input message;
    protected final @NotNull Database database;

    protected MessageExecutor(@NotNull Database database, @NotNull Message.Input message) {
        this.database = database;
        this.message = message;
    }

    /**
     * A {@link CompletableFuture} async method for execute the message.
     *
     * <p>The future will always completed with {@link MessageExecutorException} if some error occurs.
     *
     * @param buffer the buffer used to make operations
     * @param channel The channel used to update the buffer if needed.
     * */
    public abstract @NotNull CompletableFuture<Void> execute(@NotNull ByteBuffer buffer, @NotNull ReadableByteChannel channel);

    // Native

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull MessageExecutor executor = (MessageExecutor) object;
        return message.getCode() == executor.message.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message.getCode());
    }
}
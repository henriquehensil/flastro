package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.message.MessageExecutorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected boolean done;

    private @Nullable MessageExecutorException exception;

    protected MessageExecutor(@NotNull Database database, @NotNull Message.Input message) {
        this.database = database;
        this.message = message;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isCompleteSuccessfully() {
        return isDone() && getException() == null;
    }

    /**
     * @return The {@link MessageExecutorException} or {@code null} if the execute method doest not completed exceptionally
     * or if never even invoked
     * */
    public @Nullable MessageExecutorException getException() {
        return exception;
    }

    /**
     * A {@link CompletableFuture} async method for execute the message.
     *
     * <p>The future will always completed with {@link MessageExecutorException} if some error occurs.
     *
     * @param channel The channel used to update the message buffer if needed.
     * */
    public abstract @NotNull CompletableFuture<Void> execute(@Nullable ReadableByteChannel channel);

    // Native

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull MessageExecutor executor = (MessageExecutor) object;
        return done == executor.done && message.getCode() == executor.message.getCode() && Objects.equals(exception, executor.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, done, exception);
    }
}
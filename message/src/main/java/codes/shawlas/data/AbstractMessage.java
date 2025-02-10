package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed abstract class AbstractMessage implements Message permits AbstractMessage.Input, AbstractMessage.Output {

    private final @NotNull String id;
    private final @Nullable String correlationId;

    protected AbstractMessage(@NotNull String id, @Nullable String correlationId) {
        this.id = id;
        this.correlationId = correlationId;
    }

    @Override
    public final @NotNull String getId() {
        return id;
    }

    @Override
    public final @Nullable String getCorrelationId() {
        return correlationId;
    }

    // Classes

    public non-sealed abstract static class Input extends AbstractMessage implements Message.Input {

        public Input(@NotNull String id, @Nullable String correlationId) {
            super(id, correlationId);
        }

    }

    public non-sealed abstract static class Output extends AbstractMessage implements Message.Output {

        public Output(@NotNull String id, @Nullable String correlationId) {
            super(id, correlationId);
        }

    }

}

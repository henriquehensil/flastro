package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public non-sealed abstract class AbstractMessage implements Message {

    private final @NotNull String id;
    private final @Nullable String correlationId;

    public AbstractMessage(@NotNull String id, @Nullable String correlationId) {
        this.id = id;
        this.correlationId = correlationId;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @Nullable String getCorrelationId() {
        return correlationId;
    }

}

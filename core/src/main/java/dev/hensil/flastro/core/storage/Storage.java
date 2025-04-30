package dev.hensil.flastro.core.storage;

import org.jetbrains.annotations.NotNull;

public abstract class Storage {

    private final @NotNull String id;
    private final @NotNull String description;

    protected Storage(@NotNull String id, @NotNull String description) {
        this.id = id;
        this.description = description;
    }

    public final @NotNull String getId() {
        return id;
    }

    public final @NotNull String getDescription() {
        return description;
    }
}
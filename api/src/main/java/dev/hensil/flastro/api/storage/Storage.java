package dev.hensil.flastro.api.storage;

import org.jetbrains.annotations.NotNull;

public interface Storage {

    @NotNull String getId();

    byte getCode();

}
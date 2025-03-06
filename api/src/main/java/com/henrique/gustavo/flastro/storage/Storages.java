package com.henrique.gustavo.flastro.storage;

import org.jetbrains.annotations.NotNull;

public interface Storages {

    @NotNull Storage get(@NotNull String name);

    @NotNull Storage get(byte code);

    <E extends Storage> @NotNull E get(@NotNull Class<@NotNull E> storageClass);

}

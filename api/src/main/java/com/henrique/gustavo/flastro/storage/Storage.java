package com.henrique.gustavo.flastro.storage;

import org.jetbrains.annotations.NotNull;

public interface Storage {

    @NotNull String getId();

    byte getCode();

}
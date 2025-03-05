package com.henrique.gustavo.flastro.permission;

import org.jetbrains.annotations.NotNull;

public interface Permission {

    @NotNull String getName();

    byte getCode();

}
package com.henrique.gustavo.flastro.user;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {

    @NotNull String getName();

    @NotNull UUID getId();

    char @NotNull [] getPassword();

}
package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.permission.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {

    @NotNull UUID getId();

    @NotNull String getName();

    boolean has(@NotNull Permission permission);

    char @NotNull [] getPassword();

    default boolean isRoot() {
       return this instanceof Root;
    }

}
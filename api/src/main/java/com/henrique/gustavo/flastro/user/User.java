package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.permission.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {

    @NotNull UUID getId();

    @NotNull String getName();

    char @NotNull [] getPassword();

    boolean has(@NotNull Permission permission);

    default boolean isRoot() {
       return this instanceof Root;
    }

}
package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.management.Managements;
import com.henrique.gustavo.flastro.permission.Permission;
import org.jetbrains.annotations.NotNull;

public interface Root extends User {

    @NotNull Managements getManagements();

    void setName(@NotNull String name);

    void setPassword(char @NotNull [] password);

    @Override
    default boolean has(@NotNull Permission permission) {
        return true;
    }

}
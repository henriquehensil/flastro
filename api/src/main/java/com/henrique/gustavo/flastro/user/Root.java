package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.permission.Permission;
import com.henrique.gustavo.flastro.permission.Permissions;
import org.jetbrains.annotations.NotNull;

public interface Root extends User {

    @NotNull Management getManagement();

    void setName(@NotNull String name);

    void setPassword(char @NotNull [] password);

    @Override
    default boolean has(@NotNull Permission permission) {
        return true;
    }

    // Classes

    interface Management {

        @NotNull Permissions getPermissions();

        @NotNull Users getUsers();

    }

}
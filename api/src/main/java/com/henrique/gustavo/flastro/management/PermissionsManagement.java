package com.henrique.gustavo.flastro.management;

import com.henrique.gustavo.flastro.permission.Permission;
import com.henrique.gustavo.flastro.permission.Permissions;
import com.henrique.gustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;

public interface PermissionsManagement extends Management {

    @NotNull Permission get(@NotNull String name);

    @NotNull Permission get(byte code);

    void put(@NotNull User user, @NotNull Permission permission);

    void putAll(@NotNull User user, @NotNull Permissions permissions);

    void remove(@NotNull User user, @NotNull Permission permission);

    void removeAll(@NotNull User user, @NotNull Permissions permissions);

    boolean has(@NotNull User user, @NotNull Permission permission);

    boolean containsAll(@NotNull User user, @NotNull Permissions permissions);

    // Name

    @Override
    default @NotNull String getName() {
        return "Permissions Management";
    }

}
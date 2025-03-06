package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.management.Management;
import com.henrique.gustavo.flastro.management.PermissionsManagement;
import com.henrique.gustavo.flastro.management.UsersManagement;
import com.henrique.gustavo.flastro.permission.Permission;
import org.jetbrains.annotations.NotNull;

public interface Root extends User {

    void setName(@NotNull String name);

    void setPassword(char @NotNull [] password);

    @Override
    default boolean has(@NotNull Permission permission) {
        return true;
    }

    @NotNull Managements getManagements();

    // classes

    interface Managements {

        @NotNull PermissionsManagement getPermissions();

        @NotNull UsersManagement getUsers();

        @NotNull Management getManagement(@NotNull String name);

        <E extends Management> @NotNull E getManagement(@NotNull Class<E> managementClass);

    }
}
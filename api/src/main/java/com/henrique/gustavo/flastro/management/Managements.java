package com.henrique.gustavo.flastro.management;

import org.jetbrains.annotations.NotNull;

public interface Managements {

    @NotNull PermissionsManagement getPermissions();

    @NotNull UsersManagement getUsers();

    @NotNull Management getManagement(@NotNull String name);

    <E extends Management> @NotNull E getManagement(@NotNull Class<E> managementClass);

}

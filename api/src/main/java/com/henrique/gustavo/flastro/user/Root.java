package com.henrique.gustavo.flastro.user;

import com.henrique.gustavo.flastro.permission.Permissions;
import org.jetbrains.annotations.NotNull;

public interface Root extends User {

    @NotNull Permissions getPermissions();

    @NotNull Users getUsers();

    void setName(@NotNull String name);

    void setPassword(char @NotNull [] password);

}
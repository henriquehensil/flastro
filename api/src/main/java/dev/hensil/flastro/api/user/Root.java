package dev.hensil.flastro.api.user;

import org.jetbrains.annotations.NotNull;

public interface Root extends User {

    @NotNull Users getUsers();

    void setUsername(@NotNull String username);

    void setPassword(char @NotNull [] password);

}
package com.henrique.gustavo.flastro.permission;

import com.henrique.gustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Permissions {

    @NotNull Permission get(@NotNull String name);

    @NotNull Permission get(byte code);

    void put(@NotNull Permission permission, @NotNull User user);

    void remove(@NotNull Permission permission, @NotNull User user);

    void putAll(@NotNull Collection<@NotNull Permission> permissions, @NotNull User user);

    void removeAll(@NotNull Collection<@NotNull Permission> permissions, @NotNull User user);

    boolean has(@NotNull Permission permission, @NotNull User user);

}
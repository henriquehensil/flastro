package com.henrique.gustavo.flastro.permission;

import com.henrique.gustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Permissions {

    @NotNull Permission get(@NotNull String name);

    @NotNull Permission get(byte code);

    void put(@NotNull User user, @NotNull Permission permission);

    void remove(@NotNull User user, @NotNull Permission permission);

    void putAll(@NotNull User user, @NotNull Collection<@NotNull Permission> permissions);

    void removeAll(@NotNull User user, @NotNull Collection<@NotNull Permission> permissions);

}
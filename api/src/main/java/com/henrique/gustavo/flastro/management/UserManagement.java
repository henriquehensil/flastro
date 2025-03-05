package com.henrique.gustavo.flastro.management;

import com.henrique.gustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserManagement extends Management {

    @NotNull User create(@NotNull UUID uuid, @NotNull String name, char @NotNull [] password);

    @NotNull Optional<@NotNull User> get(@NotNull UUID id);

    @Unmodifiable @NotNull Set<@NotNull User> get(@NotNull String name);

    @Unmodifiable @NotNull Set<@NotNull User> get(char @NotNull [] password);

    @Unmodifiable @NotNull Collection<@NotNull User> getAll();

    boolean remove(@NotNull User user);

    boolean remove(@NotNull UUID id);

    // Name

    @Override
    default @NotNull String getName() {
        return "User Management";
    }
}
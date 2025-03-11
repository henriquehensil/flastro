package com.henrique.gustavo.flastro.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Users {

    @NotNull User create(@NotNull String name, char @NotNull [] password);

    @NotNull Optional<@NotNull User> get(@NotNull UUID id);

    @Unmodifiable @NotNull Set<@NotNull User> get(@NotNull String name);

    @Unmodifiable @NotNull Set<@NotNull User> get(char @NotNull [] password);

    @Unmodifiable @NotNull Collection<@NotNull User> getAll();

    boolean remove(@NotNull UUID id);

    boolean setName(@NotNull UUID id, @NotNull String name);

    boolean setPassword(@NotNull UUID id, char @NotNull [] password);

    default boolean remove(@NotNull User user) {
        return remove(user.getId());
    }

    default boolean setName(@NotNull User user, @NotNull String name) {
        return setName(user.getId(), name);
    }

    default boolean setPassword(@NotNull User user, char @NotNull [] password) {
        return setPassword(user.getId(), password);
    }

}
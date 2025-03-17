package com.henriquegustavo.flastro.user;

import com.henriquegustavo.flastro.exception.NoSuchUserPermissionsException;
import com.henriquegustavo.flastro.permission.UserPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public interface Users {

    @NotNull User create(@NotNull String name, char @NotNull [] password);

    @NotNull Optional<@NotNull User> get(@NotNull UUID id);

    boolean remove(@NotNull UUID id);

    boolean setName(@NotNull UUID id, @NotNull String name);

    boolean setPassword(@NotNull UUID id, char @NotNull [] password);

    @Unmodifiable @NotNull Collection<@NotNull User> getAll();

    /**
     * @throws NoSuchUserPermissionsException if doest not found
     * */
    @NotNull UserPermissions getUserPermissions(byte code);

    // defaults

    default @NotNull Set<@NotNull User> get(@NotNull String name) {
        return getAll().stream().filter(u -> u.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
    }

    default @NotNull Set<@NotNull User> get(char @NotNull [] password) {
        return getAll().stream().filter(u -> Arrays.equals(u.getPassword(), password)).collect(Collectors.toSet());
    }

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
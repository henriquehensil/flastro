package dev.hensil.flastro.core.user;

import dev.hensil.flastro.core.exception.user.NoSuchUserException;
import dev.hensil.flastro.core.exception.user.UserAlreadyExistisException;
import dev.hensil.flastro.core.permission.UserPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public interface Root extends User {

    @NotNull Users getUsers();

    @NotNull UserPermissions getUserPermissions(byte code);

    void setUsername(@NotNull String username);

    // Classes

    interface Users {

        @NotNull User create(@NotNull String name, char @NotNull [] password) throws UserAlreadyExistisException;

        @NotNull Optional<? extends @NotNull User> get(@NotNull UUID id);

        void setName(@NotNull User user, @NotNull String name) throws NoSuchUserException;

        boolean remove(@NotNull User user);

        @Unmodifiable @NotNull Collection<? extends @NotNull User> getAll();

        default @NotNull Set<? extends @NotNull User> get(@NotNull String name) {
            return getAll().stream().filter(u -> u.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
        }

        default @NotNull Set<? extends @NotNull User> get(char @NotNull [] password) {
            return getAll().stream().filter(u -> Arrays.equals(u.getPassword(), password)).collect(Collectors.toSet());
        }
    }
}
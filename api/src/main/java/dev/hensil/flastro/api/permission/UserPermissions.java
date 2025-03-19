package dev.hensil.flastro.api.permission;

import dev.hensil.flastro.api.exception.NoSuchUserException;
import dev.hensil.flastro.api.user.User;
import dev.hensil.flastro.api.user.Users;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface UserPermissions {

    @NotNull Permission getPermission();

    /**
     * @throws NoSuchUserException if the user doest not founded in {@link Users} class
     * */
    boolean put(@NotNull User user);

    boolean remove(@NotNull User user);

    @Unmodifiable @NotNull Set<@NotNull User> getUsers();

    default boolean has(@NotNull User user) {
        return getUsers().contains(user);
    }

}

package com.henriquegustavo.flastro.permission;

import com.henriquegustavo.flastro.exception.NoSuchUserException;
import com.henriquegustavo.flastro.user.User;
import com.henriquegustavo.flastro.user.Users;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface UserPermissions {

    @NotNull Permission getPermission();

    /**
     * @throws NoSuchUserException if the user doest not founded in {@link Users} class
     * */
    boolean put(@NotNull User user);

    boolean remove(@NotNull User user);

    default boolean has(@NotNull User user) {
        return getUsers().contains(user);
    }

    @Unmodifiable
    @NotNull Collection<@NotNull User> getUsers();

}

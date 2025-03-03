package codes.shawlas.data.user;

import codes.shawlas.data.permission.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserManagement {

    @NotNull Permissions getPermissions();

    @NotNull Users getUsers();

    @NotNull Accounts getAccounts();

    // Classes

    interface Users extends Iterable<@NotNull User> {

        @NotNull SimpleUser create(@NotNull UUID id, @NotNull String name, char @NotNull [] password);

        @NotNull Optional<@NotNull User> getUser(final @NotNull UUID id);

        @NotNull Collection<@NotNull User> getUser(@NotNull String name);

        @NotNull Collection<@NotNull User> getAll();

        boolean delete(@NotNull UUID id);

        default boolean delete(@NotNull User user) {
            return delete(user.getId());
        }

    }

    interface Accounts {

        default void setName(@NotNull SimpleUser user, @NotNull String name) {
            user.setName(name);
        }

        default void setPassword(@NotNull SimpleUser user, char @NotNull [] password) {
            user.setPassword(password);
        }

    }

    interface Permissions {

        void put(@NotNull SimpleUser user, @NotNull Collection<@NotNull Permission> permissions);

        void remove(@NotNull SimpleUser user, @NotNull Collection<@NotNull Permission> permissions);

        @NotNull Collection<@NotNull Permission> get(@NotNull User user);

        boolean has(@NotNull SimpleUser user, @NotNull Permission permission);

        boolean has(@NotNull SimpleUser user, @NotNull Collection<@NotNull Permission> permissions);

    }
}
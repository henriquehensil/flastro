package com.henriquegustavo.flasto.impl;

import com.henriquegustavo.flastro.permission.Permission;
import com.henriquegustavo.flastro.permission.Permissions;
import com.henriquegustavo.flastro.user.Root;
import com.henriquegustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class PermissionsImpl implements Permissions {

    private final @NotNull Root root;
    private final @NotNull Set<@NotNull UserPermissions> userPermissions;

    // todo add Permissions Provider
    PermissionsImpl(@NotNull Root root) {
        this.root = root;
        this.userPermissions = new HashSet<>();

        try {
            @NotNull Field @NotNull [] fields = Permission.class.getFields();

            for (@NotNull Field field : fields) {
                @NotNull Permission permission = (Permission) field.get(null);
                this.userPermissions.add(new UserPermissionsImpl(permission));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public @NotNull UserPermissions get(@NotNull String name) {
        return userPermissions.stream().filter(p -> p.getPermission().getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    @Override
    public @NotNull UserPermissions get(byte code) {
        return userPermissions.stream().filter(p -> p.getPermission().getCode() == code).findFirst().orElseThrow();
    }

    // Classes

    private final class UserPermissionsImpl implements UserPermissions {

        private final @NotNull Permission permission;
        private final @NotNull Set<@NotNull User> users;

        private UserPermissionsImpl(@NotNull Permission permission) {
            this.permission = permission;
            this.users = new HashSet<>();
        }

        @Override
        public @NotNull Permission getPermission() {
            return permission;
        }

        @Override
        public boolean put(@NotNull User user) {
            if (!containsInUsers(user)) return false;

            return users.add(user);
        }

        @Override
        public boolean remove(@NotNull User user) {
            return users.remove(user);
        }

        private boolean containsInUsers(@NotNull User user) {
            return root.getUsers().get(user.getId()).isPresent();
        }

        @Override
        public @Unmodifiable @NotNull Collection<@NotNull User> getUsers() {
            return Collections.unmodifiableSet(users);
        }
    }
}

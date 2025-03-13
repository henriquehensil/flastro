package com.henriquegustavo.flasto.impl;

import com.henrique.gustavo.flastro.user.User;
import com.henrique.gustavo.flastro.user.Users;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public final class UsersImpl implements Users {

    private final @NotNull Map<@NotNull UUID, @NotNull User> userMap;

    UsersImpl() {
        this.userMap = new HashMap<>();
    }

    @Override
    public @NotNull User create(@NotNull String name, char @NotNull [] password) {
        final @NotNull User user = new SimpleUser(generate(), name, password);
        userMap.put(user.getId(), user);
        return user;
    }

    private @NotNull UUID generate() {
        @NotNull UUID uuid = UUID.randomUUID();

        while (userMap.containsKey(uuid))
            uuid = UUID.randomUUID();

        return uuid;
    }

    @Override
    public @NotNull Optional<@NotNull User> get(@NotNull UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public @NotNull Set<@NotNull User> get(@NotNull String name) {
        return getAll().stream().filter(u -> u.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Set<@NotNull User> get(char @NotNull [] password) {
        return getAll().stream().filter(u -> Arrays.equals(u.getPassword(), password)).collect(Collectors.toSet());
    }

    @Override
    public @Unmodifiable @NotNull Collection<@NotNull User> getAll() {
        return Collections.unmodifiableCollection(userMap.values());
    }

    @Override
    public boolean remove(@NotNull UUID id) {
        return userMap.remove(id) != null;
    }

    @Override
    public boolean setName(@NotNull UUID id, @NotNull String name) {
        @Nullable User user = get(id).orElse(null);

        if (user == null) {
            return false;
        }

        ((SimpleUser) user).setName(name);
        return true;
    }

    @Override
    public boolean setPassword(@NotNull UUID id, char @NotNull [] password) {
        @Nullable User user = get(id).orElse(null);

        if (user == null) {
            return false;
        }

        ((SimpleUser) user).setPassword(password);
        return true;
    }
}

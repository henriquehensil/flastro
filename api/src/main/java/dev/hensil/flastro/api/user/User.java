package dev.hensil.flastro.api.user;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public interface User extends Comparable<@NotNull User> {

    @NotNull UUID getId();

    @NotNull String getName();

    @NotNull Instant getRegistration();

    char @NotNull [] getPassword();

    @Override
    int compareTo(@NotNull User anotherUser);

}
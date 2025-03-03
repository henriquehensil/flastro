package codes.shawlas.data.user;

import codes.shawlas.data.permission.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Root implements User {

    private final @NotNull UUID id;
    private @NotNull String name;
    private char @NotNull [] password;

    public Root(@NotNull UUID id, @NotNull String name, char @NotNull [] password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public char @NotNull [] getPassword() {
        return password;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setPassword(char @NotNull [] password) {
        this.password = password;
    }

    // Native

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull Root root = (Root) object;
        return Objects.equals(id, root.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // Classes

}

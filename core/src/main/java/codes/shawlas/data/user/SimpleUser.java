package codes.shawlas.data.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class SimpleUser implements User {

    private final @NotNull UUID id;
    private @NotNull String name;
    private char @NotNull [] password;

    public SimpleUser(@NotNull UUID id, @NotNull String name, char @NotNull [] password) {
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

    void setName(@NotNull String name) {
        this.name = name;
    }

    void setPassword(char @NotNull [] password) {
        this.password = password;
    }

    // Equals

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull SimpleUser that = (SimpleUser) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
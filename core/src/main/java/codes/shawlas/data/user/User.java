package codes.shawlas.data.user;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

// todo: add activity
// todo: add history
public interface User {

    @NotNull UUID getId();

    @NotNull String getName();

    char @NotNull [] getPassword();

    default boolean isRoot() {
        return this instanceof Root;
    }

}
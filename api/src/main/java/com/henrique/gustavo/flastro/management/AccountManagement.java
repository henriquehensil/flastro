package com.henrique.gustavo.flastro.management;

import com.henrique.gustavo.flastro.user.User;
import org.jetbrains.annotations.NotNull;

public interface AccountManagement extends Management {

    void setName(@NotNull User user, @NotNull String name);

    void setPassword(@NotNull User user, char @NotNull [] password);

    // Name

    @Override
    default @NotNull String getName() {
        return "Account Management";
    }

}

package ghostface.dev.database;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public interface Authentication {

    @NotNull String getUsername();

    @NotNull String getPassword();

    @NotNull InetAddress getAddress();

    int getPort();

    void setUsername(@NotNull String username) throws IllegalStateException;

    void setPassword(@NotNull String password) throws IllegalStateException;

    void setAddress(@NotNull InetAddress address) throws IllegalStateException;

    void setPort(int port);
}

package codes.shawlas.impl.core;

import codes.shawlas.database.Authentication;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class AuthenticationImpl implements Authentication {

    private final @NotNull InetAddress address;
    private final @Range(from = 0, to = 65535) int port;
    private final @NotNull String username;
    private final @NotNull String password;

    public AuthenticationImpl(
            @NotNull InetAddress address,
            @Range(from = 0, to = 65535) int port,
            @NotNull String username,
            @NotNull String password
    ) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public AuthenticationImpl(@NotNull InetSocketAddress address, @NotNull String username, @NotNull String password) {
        this.address = address.getAddress();
        this.port = address.getPort();
        this.username = username;
        this.password = password;
    }

    @Override
    public @NotNull InetAddress getAddress() {
        return address;
    }

    @Override
    public @Range(from = 0, to = 65535) int getPort() {
        return port;
    }

    @Override
    public @NotNull String getUsername() {
        return username;
    }

    @Override
    public @NotNull String getPassword() {
        return password;
    }
}

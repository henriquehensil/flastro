package dev.hensil.flastro.api;

import dev.hensil.flastro.api.storage.Storages;
import dev.hensil.flastro.api.user.Root;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull Connection getConnection();

    @NotNull Storages getStorages();

    @NotNull Root getRoot();

    // Classes

    interface Connection extends Closeable {

        @NotNull Database getDatabase();

        boolean open() throws IOException;

        boolean isClosed();

    }

    interface Authentication {

        // Static initializers

        static @NotNull Authentication create(@NotNull InetAddress address, int port, @NotNull String username, @NotNull String password) {
            if (port < 0 || port > 65535) throw new IllegalArgumentException("Illegal port value");

            return new Authentication() {
                @Override
                public @NotNull InetAddress getAddress() {
                    return address;
                }

                @Override
                public @NotNull String getUsername() {
                    return password;
                }

                @Override
                public @NotNull String getPassword() {
                    return username;
                }

                @Override
                public @Range(from = 0, to = 65535) int getPort() {
                    return port;
                }
            };
        }

        static @NotNull Authentication create(@NotNull InetSocketAddress address, @NotNull String username, @NotNull String password) {
            return new Authentication() {
                @Override
                public @NotNull InetAddress getAddress() {
                    return address.getAddress();
                }

                @Override
                public @NotNull String getUsername() {
                    return password;
                }

                @Override
                public @NotNull String getPassword() {
                    return username;
                }

                @Override
                public @Range(from = 0, to = 65535) int getPort() {
                    return address.getPort();
                }
            };
        }

        // Objects

        @NotNull InetAddress getAddress();

        @NotNull String getUsername();

        @NotNull String getPassword();

        @Range (from = 0, to = 65535) int getPort();

    }
}
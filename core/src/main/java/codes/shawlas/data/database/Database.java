package codes.shawlas.data.database;

import codes.shawlas.data.impl.FileStorage;
import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.net.InetAddress;

public interface Database {

    @NotNull Connection getConnection();

    @NotNull Storages getStorages();

    // Classes

    interface Storages {

        @NotNull FileStorage getFileStorage();

        /**
         * @throws IllegalArgumentException if the id doest not exists
         * */
        @NotNull Storage getStorage(@NotNull String id);

        boolean contains(@NotNull String id);

    }

    interface Authentication {

        static @NotNull Authentication create(@NotNull InetAddress address, int port) {
            if (port < 0 || port > 65535) throw new IllegalArgumentException("Illegal port value");

            return new Authentication() {
                @Override
                public @NotNull InetAddress getAddress() {
                    return address;
                }

                @Override
                public @NotNull String getUsername() {
                    return "";
                }

                @Override
                public @NotNull String getPassword() {
                    return "";
                }

                @Override
                public @Range(from = 0, to = 65535) int getPort() {
                    return port;
                }
            };
        }

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

        @NotNull InetAddress getAddress();

        @NotNull String getUsername();

        @NotNull String getPassword();

        @Range(from = 0, to = 65535) int getPort();

    }

    interface Connection {

        @NotNull Database getDatabase();

        @NotNull Authentication getAuthentication();

        boolean start() throws IOException;

        boolean stop() throws IOException;

        boolean isClosed();

    }
}
package codes.shawlas.data;

import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface Database {

    // Objects

    @NotNull Authentication getAuthentication();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    boolean start() throws IOException;

    boolean stop() throws IOException;

    // Classes

    interface Authentication {

        // Static initializers

        static @NotNull Authentication create(@NotNull SocketAddress address, int port, @NotNull String username, @NotNull String password) {
            if (port < 0) throw new IllegalArgumentException("Invalid port value");

            return new Authentication() {
                @Override
                public @NotNull SocketAddress getAddress() {
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
            };
        }

        static @NotNull Authentication create(@NotNull InetSocketAddress address, @NotNull String username, @NotNull String password) {
            return new Authentication() {
                @Override
                public @NotNull SocketAddress getAddress() {
                    return address;
                }

                @Override
                public @Range(from = 0, to = 65535) int getPort() {
                    return address.getPort();
                }

                @Override
                public @NotNull String getUsername() {
                    return username;
                }

                @Override
                public @NotNull String getPassword() {
                    return password;
                }
            };
        }

        // Objects

        @NotNull SocketAddress getAddress();

        @Range(from = 0, to = 65535) int getPort();

        @NotNull String getUsername();

        @NotNull String getPassword();

    }
}
package codes.shawlas.data;

import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface Database {

    // Static Initializers

    static @NotNull Database create(@NotNull Authentication authentication) {
        try {
            // noinspection unchecked
            @NotNull Class<? extends TableStorage> tableStorage = (Class<? extends TableStorage>) Class.forName("codes.shawlas.data.impl.table.SimpleTableStorage");
            @NotNull Constructor<? extends TableStorage> tsConstructor = tableStorage.getDeclaredConstructor();

            // Todo: NestStorage and FileStorage
            @NotNull TableStorage ts = tsConstructor.newInstance();

            // Instantiation
            return new Database() {
                @Override
                public @NotNull Authentication getAuthentication() {
                    return authentication;
                }

                @Override
                public @NotNull TableStorage getTableStorage() {
                    return ts;
                }
            };
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the implementation class", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot instantiate the constructor of implementation class", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementation class");
        }
    }

    // Objects

    @NotNull Authentication getAuthentication();

    @NotNull TableStorage getTableStorage();

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

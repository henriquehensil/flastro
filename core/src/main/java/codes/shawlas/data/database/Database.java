package codes.shawlas.data.database;

import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;

public interface Database {

    @NotNull Connection getConnection();

    @NotNull Storages getStorages();

    // Classes

    interface Storages {

        /**
         * @throws IllegalArgumentException if the id doest not exists
         * */
        @NotNull Storage getStorage(@NotNull String id);

        boolean contains(@NotNull String id);

    }

    interface Authentication {

        @NotNull InetAddress getAddress();

        @NotNull String getUsername();

        @NotNull String getPassword();

    }

    interface Connection {

        @NotNull Database getDatabase();

        @NotNull Authentication getAuthentication();

        boolean start() throws IOException;

        boolean stop() throws IOException;

        boolean isClosed();

    }
}
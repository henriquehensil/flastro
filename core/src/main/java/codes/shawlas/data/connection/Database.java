package codes.shawlas.data.connection;

import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.nest.NestStorage;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.Channel;

public interface Database {

    @NotNull Connection getConnection();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    /**
     * A representation of database connection in the both sides
     * */
    interface Connection extends Channel {

        @NotNull Authentication getAuthentication();

        void open() throws IOException;

        @Override
        boolean isOpen();

        @Override
        void close() throws IOException;

    }
}
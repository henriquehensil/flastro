package codes.shawlas.data.connection;

import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.nest.NestStorage;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface Database {

    @NotNull Connection getConnection();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    @NotNull NestStorage getNestStorage();

    /**
     * A representation of database connection in the both sides
     * */
    interface Connection extends MessageConnection, Channel, WritableByteChannel, ReadableByteChannel {

        @NotNull Authentication getAuthentication();

        /**
         * @throws UnsupportedOperationException if these read operation is not supported by this connection
         * */
        @Override
        @UnknownNullability Object read() throws IOException;

        /**
         * @throws UnsupportedOperationException if these write operation is not supported by this connection
         * */
        @Override
        void write(@NotNull Object message) throws IOException;

        /**
         * @throws UnsupportedOperationException if these read operation is not supported by this connection
         * */
        @Override
        int read(@NotNull ByteBuffer dst) throws IOException;

        /**
         * @throws UnsupportedOperationException if these write operation is not supported by this connection
         * */
        @Override
        int write(@NotNull ByteBuffer src) throws IOException;

        // Channel

        void open() throws IOException;

        @Override
        boolean isOpen();

        @Override
        void close() throws IOException;

    }

    /**
     * A Message Connection is a high-level readable and writable operation for manipulating messages.
     * */
    interface MessageConnection {

        @UnknownNullability Object read() throws IOException;

        void write(@NotNull Object message) throws IOException;

    }

}
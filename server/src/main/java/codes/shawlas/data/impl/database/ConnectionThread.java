package codes.shawlas.data.impl.database;

import codes.shawlas.data.exception.IllegalMessageException;
import codes.shawlas.data.message.MessageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import codes.shawlas.data.impl.database.DatabaseImpl.ConnectionImpl;

final class ConnectionThread extends Thread {

    private final @NotNull DatabaseImpl database;
    private final @NotNull ServerSocketChannel server;
    private final @NotNull Selector selector;

    ConnectionThread(@NotNull ConnectionImpl connection) {
        if (connection.isClosed()) throw new IllegalStateException("Server is not active");

        final @Nullable ServerSocketChannel server = connection.getServer();
        final @Nullable Selector selector = connection.getSelector();

        assert server != null;
        assert selector != null;

        this.server = server;
        this.selector = selector;
        this.database = connection.getDatabase();
    }

    @Override
    public void run() {
        while (server.isOpen()) {
            @NotNull Iterator<@NotNull SelectionKey> keyIterator;
            try {
                int s = selector.select();
                if (s == 0) continue;
                keyIterator = selector.selectedKeys().iterator();
            } catch (IOException e) {
                continue;
            } catch (ClosedSelectorException e) {
                break;
            }

            while (keyIterator.hasNext()) {
                @NotNull SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    accept();
                }

                if (key.isReadable()) {

                }
            }
        }
    }

    private void read(@NotNull SelectionKey key) {
        @NotNull SocketChannel channel = (SocketChannel) key.channel();
        @NotNull ByteBuffer buffer = ByteBuffer.allocate(4196);

        try {
            int read = channel.read(buffer);

            if (read == -1) {
                throw new ClosedChannelException();
            }

            CompletableFuture.runAsync(() -> MessageProvider.deserialize(buffer).execute(database, buffer, channel));

        } catch (ClosedChannelException | IllegalMessageException e) {
            close(channel);
        } catch (IOException ignore) {
            // todo Exception Message
        }
    }

    private void accept() {
        @Nullable SocketChannel client = null;
        try {
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } catch (Throwable e) {
            if (client != null) {
                close(client);
            }
        }
    }

    private void close(@NotNull SocketChannel client) {
        try {
            getConnection().getClients().remove(client);
            client.close();
        } catch (IOException ignore) {
            // ignored
        }
    }

    private @NotNull ConnectionImpl getConnection() {
        return (ConnectionImpl) database.getConnection();
    }
}

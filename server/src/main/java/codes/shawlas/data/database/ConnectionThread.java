package codes.shawlas.data.database;

import codes.shawlas.data.exception.message.MessageReaderException;
import codes.shawlas.data.exception.message.NoSuchReaderException;
import codes.shawlas.data.database.DatabaseImpl.ConnectionImpl;
import codes.shawlas.data.message.Message;
import codes.shawlas.data.message.MessageReader;
import codes.shawlas.data.message.content.ExceptionMessage;
import codes.shawlas.data.message.content.SuccessMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
                    read(key);
                }
            }
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

    private void read(@NotNull SelectionKey key) {
        @NotNull SocketChannel channel = (SocketChannel) key.channel();
        @NotNull ByteBuffer buffer = ByteBuffer.allocate(8192);

        try {
            int read = channel.read(buffer);

            if (read == -1) {
                throw new ClosedChannelException();
            }

            @NotNull AtomicReference<Message.@NotNull Output> output = new AtomicReference<>();

            // Call the message reader
            MessageReader.getInstance(buffer).nextMessage().whenComplete((input, e1) -> {
                if (e1 != null) {
                    throw (MessageReaderException) e1.getCause();
                }

                // Call the message executor
                input.getExecutor(database).execute(buffer, channel).whenComplete((result, e2) -> {
                    if (e2 != null) {
                        output.set(new ExceptionMessage(UUID.randomUUID(), input, e2.getCause()));
                    } else {
                        output.set(new SuccessMessage(UUID.randomUUID(), input, "Operation has been successfully"));
                    }
                });

                try {
                    channel.write(output.get().serialize());
                } catch (IOException ignored) {
                    // todo Exception Message
                }

            });
        } catch (NoSuchReaderException | MessageReaderException e) {
            // todo Disconnect message
            close(channel);
        } catch (ClosedChannelException e) {
            close(channel);
        } catch (IOException ignored) {
            // todo Exception Message
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
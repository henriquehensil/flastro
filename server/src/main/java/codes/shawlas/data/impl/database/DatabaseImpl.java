package codes.shawlas.data.impl.database;

import codes.shawlas.data.database.Database;

import codes.shawlas.data.impl.FileStorage;
import codes.shawlas.data.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DatabaseImpl implements Database {

    private final @NotNull Authentication authentication;
    private final @NotNull Connection connection;
    private final @NotNull Storages storages;

    public DatabaseImpl(@NotNull Authentication authentication, @NotNull Path path) throws IOException {
        this.authentication = authentication;
        this.connection = new ConnectionImpl();
        this.storages = new StoragesImpl(path);
    }

    @Override
    public @NotNull Connection getConnection() {
        return connection;
    }

    @Override
    public @NotNull Storages getStorages() {
        return storages;
    }

    // Classes

    public static final class StoragesImpl implements Storages {

        private final @NotNull FileStorage fileStorage;

        public StoragesImpl(@NotNull Path path) throws IOException {
            this.fileStorage = new FileStorage(path);
        }

        @Override
        public @NotNull FileStorage getFileStorage() {
            return fileStorage;
        }

        @Override
        public @NotNull Storage getStorage(@NotNull String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(@NotNull String id) {
            throw new UnsupportedOperationException();
        }
    }

    final class ConnectionImpl implements Connection {

        private final @NotNull Set<@NotNull SocketChannel> clients = ConcurrentHashMap.newKeySet();
        private @Nullable ServerSocketChannel server;
        private @Nullable Selector selector;
        private @Nullable Thread thread;

        @Override
        public @NotNull DatabaseImpl getDatabase() {
            return DatabaseImpl.this;
        }

        @Override
        public @NotNull Authentication getAuthentication() {
            return getDatabase().authentication;
        }

        @Override
        public synchronized boolean start() throws IOException {
            if (isClosed()) return false;

            final @NotNull ServerSocketChannel server = ServerSocketChannel.open();
            final @NotNull Selector selector = Selector.open();
            final @NotNull InetSocketAddress address = new InetSocketAddress(authentication.getAddress(), authentication.getPort());

            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            server.bind(address);

            this.server = server;
            this.selector = selector;
            this.thread = new ConnectionThread(this);
            this.thread.start();

            return true;
        }

        @Override
        public synchronized boolean stop() throws IOException {
            if (isClosed()) return false;

            assert this.thread != null;
            assert this.server != null;
            assert this.selector != null;

            this.thread.interrupt();
            this.thread = null;

            for (@NotNull SocketChannel channel : clients) {
                clients.remove(channel);
                channel.close();
            }

            this.server.close();
            this.server = null;

            this.selector.close();
            this.selector = null;

            return false;
        }

        @Override
        public boolean isClosed() {
            return server == null && selector == null && thread == null;
        }

        @NotNull Set<@NotNull SocketChannel> getClients() {
            return clients;
        }

        @Nullable ServerSocketChannel getServer() {
            return server;
        }

        @Nullable Selector getSelector() {
            return selector;
        }
    }
}
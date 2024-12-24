package codes.shawlas;

import codes.shawlas.impl.core.*;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public final class FieldsProviders {

    private static final @NotNull DatabaseImpl database;

    static {
        database = new DatabaseImpl(new AuthenticationImpl(new InetSocketAddress(80), "user", "password"));
    }

    public static @NotNull FileStorageImpl getFileStorage() {
        return database.getFileStorage();
    }

    public static @NotNull TableStorageImpl getTableStorage() {
        return database.getTableStorage();
    }

    public static @NotNull NestStorageImpl getNestStorage() {
        return database.getNestStorage();
    }

    // Constructor

    private FieldsProviders() {
        throw new UnsupportedOperationException();
    }
}

package codes.shawlas.impl.core;

import codes.shawlas.database.Authentication;
import codes.shawlas.database.Database;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public final class DatabaseImpl implements Database {

    private final @NotNull AuthenticationImpl authentication;
    private final @NotNull FileStorageImpl fileStorage;
    private final @NotNull NestStorageImpl nestStorage;
    private final @NotNull TableStorageImpl tableStorage;

    public DatabaseImpl(@NotNull AuthenticationImpl authentication) {
        this.authentication = authentication;
        this.fileStorage = new FileStorageImpl(this, Paths.get(System.getProperty("user.dir"), "/root"));
        this.nestStorage = new NestStorageImpl(this);
        this.tableStorage = new TableStorageImpl(this);
    }

    @Override
    public @NotNull Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public @NotNull FileStorageImpl getFileStorage() {
        return fileStorage;
    }

    @Override
    public @NotNull NestStorageImpl getNestStorage() {
        return nestStorage;
    }

    @Override
    public @NotNull TableStorageImpl getTableStorage() {
        return tableStorage;
    }
}

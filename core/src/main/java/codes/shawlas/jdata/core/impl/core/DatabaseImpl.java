package codes.shawlas.jdata.core.impl.core;

import codes.shawlas.jdata.core.database.Authentication;
import codes.shawlas.jdata.core.database.Database;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public final class DatabaseImpl implements Database {

    private final @NotNull Authentication authentication;
    private final @NotNull FileStorageImpl fileStorage;
    private final @NotNull NestStorageImpl nestStorage;
    private final @NotNull TableStorageImpl tableStorage;

    public DatabaseImpl(@NotNull Authentication authentication) {
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

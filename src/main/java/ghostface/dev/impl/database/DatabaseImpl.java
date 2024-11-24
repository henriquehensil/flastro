package ghostface.dev.impl.database;

import ghostface.dev.database.Authentication;
import ghostface.dev.database.Database;
import ghostface.dev.impl.storage.FileStorageImpl;
import ghostface.dev.impl.storage.NestStorageImpl;
import ghostface.dev.impl.storage.TableStorageImpl;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public final class DatabaseImpl implements Database {

    private final @NotNull Authentication authentication;
    private final @NotNull FileStorageImpl fileStorage;
    private final @NotNull TableStorageImpl tableStorage;
    private final @NotNull NestStorageImpl nestStorage;

    public DatabaseImpl(@NotNull Authentication authentication) {
        this.authentication = authentication;
        this.fileStorage = new FileStorageImpl(this, Paths.get(System.getProperty("user.dir"), "root"));
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
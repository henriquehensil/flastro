package codes.shawlas.data.connection;

import codes.shawlas.data.file.FileStorage;
import codes.shawlas.data.table.TableStorage;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

public interface Database {

    @NotNull Authentication getAuthentication();

    @NotNull TableStorage getTableStorage();

    @NotNull FileStorage getFileStorage();

    @NotNull Connection getConnection();

    // Classes

    interface Connection extends Closeable {

        void open() throws IOException;

    }

}
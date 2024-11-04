package ghostface.dev.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.net.InetAddress;

public interface Authentication {

    @NotNull String username();

    @NotNull String password();

    @NotNull InetAddress hostname();

    @Range(from = 0, to = 65535)
    int port();

}

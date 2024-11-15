package ghostface.dev.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.net.InetAddress;

public interface Authentication {

    @NotNull InetAddress getAddress();

    @Range(from = 0, to = 65535)
    int getPort();

    @NotNull String getUsername();

    @NotNull String getPassword();
}

package codes.shawlas.data.message;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface Message {

    @NotNull String getId();

    @NotNull InputStream serialize();

}
package codes.shawlas.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystemException;

public interface FileOwner extends CharSequence {

    @NotNull String getName();

    void set(@NotNull String owner) throws FileSystemException;

}

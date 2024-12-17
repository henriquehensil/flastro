package codes.shawlas.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;

public final class FileUserOwner implements FileOwner {

    private @NotNull String user;
    private final @NotNull File file;

    public FileUserOwner(@NotNull File file) throws IOException {
        this.file = file;
        this.user = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class).getOwner().getName();
    }

    @Override
    public @NotNull String getName() {
        return user;
    }

    @Override
    public @NotNull String toString() {
        return user;
    }

    @Override
    public void set(@NotNull String owner) throws FileSystemException {
        try {
            Files.setOwner(file.toPath(), () -> owner);
            this.user = owner;
        } catch (IOException e) {
            throw new FileSystemException(e.getMessage());
        }
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int i) {
        return toString().charAt(i);
    }

    @Override
    public @NotNull CharSequence subSequence(int i, int i1) {
        return toString().subSequence(i, i1);
    }
}

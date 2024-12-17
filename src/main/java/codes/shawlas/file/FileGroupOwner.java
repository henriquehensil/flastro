package codes.shawlas.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;

public final class FileGroupOwner implements FileOwner {

    private @NotNull String group;
    private final @NotNull File file;

    public FileGroupOwner(@NotNull File file) throws IOException {
        this.file = file;
        this.group = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class).readAttributes().group().getName();
    }

    @Override
    public @NotNull String getName() {
        return group;
    }

    @Override
    public @NotNull String toString() {
        return group;
    }

    @Override
    public void set(@NotNull String group) throws FileSystemException {
        try {
            Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class).setGroup(() -> group);
            this.group = group;
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

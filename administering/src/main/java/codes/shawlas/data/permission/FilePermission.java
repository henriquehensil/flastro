package codes.shawlas.data.permission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public interface FilePermission {

    // Static initializer

    static @NotNull FilePermission create(boolean canRead, boolean canWrite, boolean canExecute) {
        return new FilePermission() {
            private boolean read = canRead;
            private boolean write = canWrite;
            private boolean execute = canExecute;

            @Override
            public boolean canRead() {
                return read;
            }

            @Override
            public boolean canWrite() {
                return write;
            }

            @Override
            public boolean canExecute() {
                return execute;
            }

            @Override
            public void setPermission(boolean canRead, boolean canWrite, boolean canExecute) {
                this.read = canRead;
                this.write = canWrite;
                this.execute = canExecute;
            }

            // Native

            @Override
            public @NotNull String toString() {
                return String.valueOf(getCode());
            }

            @Override
            public boolean equals(@Nullable Object obj) {
                if (obj instanceof FilePermission) {
                    @NotNull FilePermission that = (FilePermission) obj;
                    return Objects.equals(this.getCode(), that.getCode());
                }

                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(getCode());
            }
        };
    }

    static @NotNull FilePermission parse(int code) {
        if (code < 0 || code > 7) throw new IllegalArgumentException("Invalid permission code");

        return create(code >= 4, code >= 2, code >= 1);
    }

    // Objects

    boolean canRead();

    boolean canWrite();

    boolean canExecute();

    void setPermission(boolean canRead, boolean canWrite, boolean canExecute);

    default void setPermission(int code) {
        if (code < 0 || code > 7) throw new IllegalArgumentException("Invalid permission code");

        setPermission(code >= 4, code >= 2, code >= 1);
    }

    default @Range(from = 0, to = 7) int getCode() {
        int n = 0;
        if (canRead()) n += 4; if (canWrite()) n += 2; if (canExecute()) n ++;

        return n;
    }

    @Override
    @NotNull String toString();

    @Override
    boolean equals(@Nullable Object o);

    @Override
    int hashCode();
}
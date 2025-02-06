package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public sealed class Request permits Request.Server, Request.Client {

    private final @Range(from = 0, to = Long.MAX_VALUE) int id;

    protected Request(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid id");
        this.id = id;
    }

    public final @Range(from = 0, to = Long.MAX_VALUE) int getId() {
        return id;
    }

    public final boolean isServer() {
        return this instanceof Server;
    }

    public final boolean isClient() {
        return this instanceof Client;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        @NotNull Request request = (Request) object;
        return id == request.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // Classes

    public non-sealed abstract static class Server extends Request {

        public Server(int id) {
            super(id);
        }

        public abstract @NotNull Message.Output data(@NotNull Message.Input message);

    }

    public non-sealed abstract static class Client extends Request {

        public Client(int id) {
            super(id);
        }

    }
}

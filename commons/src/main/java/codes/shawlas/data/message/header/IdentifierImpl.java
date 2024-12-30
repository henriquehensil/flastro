package codes.shawlas.data.message.header;

import codes.shawlas.data.MessageModel;
import codes.shawlas.data.exception.MessageParseException;
import codes.shawlas.data.header.Action;
import codes.shawlas.data.header.Identifier;
import codes.shawlas.data.message.MessageStructure;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public final class IdentifierImpl implements Identifier {

    private final @Range(from = 0, to = Long.MAX_VALUE) int id;

    public IdentifierImpl(int id) {
        if (id < 0) throw new IllegalArgumentException("Invalid Id");
        this.id = id;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) int getId() {
        return id;
    }

    @Override
    public @NotNull MessageModel getModel(@NotNull Action action, @NotNull JsonObject data) throws MessageParseException {
        return MessageStructure.parse(action + "\r\n" + this, data);
    }

    @Override
    public @NotNull String toString() {
        return "IDENTIFIER: " + id;
    }

    // Native

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final @NotNull IdentifierImpl that = (IdentifierImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

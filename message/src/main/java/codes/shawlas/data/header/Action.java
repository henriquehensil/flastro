package codes.shawlas.data.header;

import codes.shawlas.data.MessageModel;
import codes.shawlas.data.exception.MessageParseException;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface Action extends CharSequence {

    @NotNull String getType();

    @NotNull String getItem();

    @NotNull MessageModel getModel(@NotNull Identifier identifier, @NotNull JsonObject data) throws MessageParseException;

    // CharSequence implementations

    @Override
    default int length() {
        return toString().length();
    }

    @Override
    default char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    default @NotNull CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    @Override
    @NotNull String toString();
}

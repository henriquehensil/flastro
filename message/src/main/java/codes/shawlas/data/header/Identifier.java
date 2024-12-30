package codes.shawlas.data.header;

import codes.shawlas.data.MessageModel;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface Identifier extends CharSequence {

    @Range(from = 0, to = Long.MAX_VALUE) int getId();

    @NotNull MessageModel getModel(@NotNull Action action, @NotNull JsonObject data);

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

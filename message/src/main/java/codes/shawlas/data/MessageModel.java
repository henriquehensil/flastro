package codes.shawlas.data;

import codes.shawlas.data.header.Action;
import codes.shawlas.data.header.Identifier;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * <p>An Interface that provides the message structure model.</p>
 * <p>In other words, {@code MessageModel} defines the syntax rule of the message</p>
 * */
public interface MessageModel extends CharSequence {

    @NotNull Action getAction();

    /**
     * @return The message identifier.
     * <br>
     * <p>This is not just limited by a simple identifier and can be able to linked I/O messages</p>
     * */
    @NotNull Identifier getIdentifier();

    /**
     * @return The message body data
     * */
    @NotNull JsonObject getData();

    /**
     * @return The Message without data
     * */
    @NotNull String getHeader();

    /**
     * @return The Message formated
     * */
    @NotNull String getFull();

    boolean isFinished();

    // CharSequence Implementations

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

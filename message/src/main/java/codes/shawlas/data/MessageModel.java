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

    @NotNull Identifier getIdentifier();

    @NotNull JsonObject getData();

    @NotNull String getHeader();

    /**
     * @return The Message formated
     * */
    @NotNull String getFull();

    default byte @NotNull [] getBytes() {
        return getFull().getBytes();
    }

    // CharSequence Implementations

    @Override
    int length();

    @Override
    char charAt(int index);

    @Override
    @NotNull CharSequence subSequence(int start, int end);

    @Override
    @NotNull String toString();
}

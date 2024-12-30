package codes.shawlas.data.message;

import codes.shawlas.data.MessageModel;
import codes.shawlas.data.exception.message.MessageParseException;
import codes.shawlas.data.message.header.ActionImpl;
import codes.shawlas.data.message.header.IdentifierImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MessageStructure implements MessageModel {

    // Static initializer

    public static boolean isValidHeader(@NotNull String header) {
        @NotNull String @NotNull [] parts = header.split("\r\n");
        if (parts.length != 2) return false;

        @NotNull String @NotNull [] action = parts[0].split("\\s");
        if (action.length != 2) {
            return false;
        } else if (!ActionImpl.contains(new ActionImpl(action[0], action[1]))) {
            return false;
        } else try {
            return Integer.parseInt(parts[1].replace("IDENTIFIER: ", "")) >= 0;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isValid(@NotNull String message) {
        @NotNull String @NotNull [] parts = message.split("\r\n\r\n");
        if (!isValidHeader(parts[0])) {
            return false;
        } else try {
            @NotNull JsonObject data = JsonParser.parseString(parts[1]).getAsJsonObject();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static @NotNull MessageStructure parse(@NotNull String message) {
        @NotNull String @NotNull [] parts = message.split("\r\n\r\n");
        if (!isValidHeader(parts[0])) {
            throw new MessageParseException("Cannot parse '" + message + "' as a valid header");
        } else try {
            @NotNull JsonObject data = JsonParser.parseString(parts[1]).getAsJsonObject();

            @NotNull String @NotNull [] header = parts[0].split("\r\n");

            @NotNull String @NotNull [] actions = header[0].split("\\s");
            int id = Integer.parseInt(header[1].replace("IDENTIFIER: ", ""));

            @Nullable ActionImpl act = ActionImpl.get(actions[0], actions[1]).orElse(null);
            if (act == null) throw new MessageParseException("Cannot find the action in the message");

            return new MessageStructure(act, new IdentifierImpl(id), data);
        } catch (Throwable e) {
            throw new MessageParseException("Cannot parse '" + message + "' as a valid message: ", e);
        }
    }

    public static @NotNull MessageStructure parse(@NotNull String message, @NotNull JsonObject data) {
        if (!isValidHeader(message)) {
            throw new MessageParseException("Cannot parse '" + message + "' as a valid header");
        } else try {
            @NotNull String @NotNull [] header = message.split("\r\n");
            @NotNull String @NotNull [] actions = header[0].split("\\s");
            int id = Integer.parseInt(header[1].replace("IDENTIFIER: ", ""));

            @Nullable ActionImpl act = ActionImpl.get(actions[0], actions[1]).orElse(null);
            if (act == null) throw new MessageParseException("Cannot find the action in the message");

            return new MessageStructure(act, new IdentifierImpl(id), data);
        } catch (Throwable e) {
            throw new MessageParseException("Cannot parse '" + message + "' as a valid message: " + e.getMessage());
        }
    }

    // Object

    private final @NotNull ActionImpl action;
    private final @NotNull IdentifierImpl identifier;
    private final @NotNull JsonObject data;

    public MessageStructure(@NotNull ActionImpl action, @NotNull IdentifierImpl identifier, @NotNull JsonObject data) {
        this.action = action;
        this.identifier = identifier;
        this.data = data;
    }

    @Override
    public @NotNull ActionImpl getAction() {
        return action;
    }

    @Override
    public @NotNull IdentifierImpl getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull JsonObject getData() {
        return data;
    }

    @Override
    public @NotNull String getHeader() {
        return action + "\r\n" + identifier;
    }

    @Override
    public @NotNull String getFull() {
        return getHeader() + "\r\n\r\n" + data;
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    @Override
    public @NotNull String toString() {
        return getFull();
    }
}

package codes.shawlas.data.message.header;

import codes.shawlas.data.exception.MessageParseException;
import codes.shawlas.data.header.Action;
import codes.shawlas.data.header.Identifier;
import codes.shawlas.data.message.MessageStructure;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ActionImpl implements Action {

    // Static initializers

    private static final @NotNull Set<@NotNull ActionImpl> actions = new HashSet<>();

    public static boolean add(@NotNull ActionImpl action) {
        return actions.add(action);
    }

    public static boolean remove(@NotNull ActionImpl action) {
        return actions.remove(action);
    }

    public static boolean contains(@NotNull ActionImpl action) {
        return actions.contains(action);
    }

    public static @NotNull Optional<@NotNull ActionImpl> get(@NotNull String type, @NotNull String item) {
        return toSet().stream().filter(action -> action.type.equalsIgnoreCase(type) && action.item.equalsIgnoreCase(item)).findFirst();
    }

    public static @Unmodifiable @NotNull Set<@NotNull ActionImpl> toSet() {
        return Set.copyOf(actions);
    }

    // Providers

    public static final @NotNull ActionImpl AUTHENTICATION = new ActionImpl("AUTH", "DATABASE");

    public static final @NotNull ActionImpl GET_FILE = new ActionImpl("GET", "FILE");
    public static final @NotNull ActionImpl GET_TABLE = new ActionImpl("GET", "TABLE");
    public static final @NotNull ActionImpl GET_ELEMENT = new ActionImpl("GET", "ELEMENT");
    public static final @NotNull ActionImpl GET_COLUMN = new ActionImpl("GET", "COLUMN");
    public static final @NotNull ActionImpl GET_NEST = new ActionImpl("GET", "NEST");

    public static final @NotNull ActionImpl UPDATE_FILE = new ActionImpl("UPDATE", "FILE");
    public static final @NotNull ActionImpl UPDATE_TABLE = new ActionImpl("UPDATE", "TABLE");
    public static final @NotNull ActionImpl UPDATE_ELEMENT = new ActionImpl("UPDATE", "ELEMENT");
    public static final @NotNull ActionImpl UPDATE_COLUMN = new ActionImpl("UPDATE", "COLUMN");
    public static final @NotNull ActionImpl UPDATE_NEST = new ActionImpl("UPDATE", "NEST");

    public static final @NotNull ActionImpl DELETE_FILE = new ActionImpl("DELETE", "FILE");
    public static final @NotNull ActionImpl DELETE_TABLE = new ActionImpl("DELETE", "TABLE");
    public static final @NotNull ActionImpl DELETE_ELEMENT = new ActionImpl("DELETE", "ELEMENT");
    public static final @NotNull ActionImpl DELETE_COLUMN = new ActionImpl("DELETE", "COLUMN");
    public static final @NotNull ActionImpl DELETE_NEST = new ActionImpl("DELETE", "NEST");

    public static final @NotNull ActionImpl CREATE_FILE = new ActionImpl("CREATE", "FILE");
    public static final @NotNull ActionImpl CREATE_TABLE = new ActionImpl("CREATE", "TABLE");
    public static final @NotNull ActionImpl CREATE_ELEMENT = new ActionImpl("CREATE", "ELEMENT");
    public static final @NotNull ActionImpl CREATE_COLUMN = new ActionImpl("CREATE", "COLUMN");
    public static final @NotNull ActionImpl CREATE_COLUMN_KEY = new ActionImpl("CREATE", "COLUMN KEY");
    public static final @NotNull ActionImpl CREATE_NEST = new ActionImpl("CREATE", "NEST");

    // Loaders

    static {
        for (@NotNull Field field : ActionImpl.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                try {
                    add((ActionImpl) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot load the action fields", e);
                }
            }
        }
    }

    // Objects

    private final @NotNull String type;
    private final @NotNull String item;

    public ActionImpl(@NotNull String type, @NotNull String item) {
        this.type = type.toUpperCase();
        this.item = item.toUpperCase();
    }

    @Override
    public @NotNull String getType() {
        return type;
    }

    @Override
    public @NotNull String getItem() {
        return item;
    }

    @Override
    public @NotNull MessageStructure getModel(@NotNull Identifier identifier, @NotNull JsonObject data) throws MessageParseException {
        return MessageStructure.parse(this + "\r\n"  + identifier,  data);
    }

    @Override
    public @NotNull String toString() {
        return type + " " + item;
    }

    // Native

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final ActionImpl action = (ActionImpl) o;
        return Objects.equals(type, action.type) && Objects.equals(item, action.item) && Objects.equals(toString(), action.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item);
    }
}

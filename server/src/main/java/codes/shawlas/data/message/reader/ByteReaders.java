package codes.shawlas.data.message.reader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class ByteReaders {

    private static final @NotNull Map<@NotNull Byte, @NotNull Constructor<@NotNull ByteReader>> map = new HashMap<>();

    public static boolean contains(byte code) {
        return map.containsKey(code);
    }

    public static @Nullable Constructor<@NotNull ByteReader> put(byte code, @NotNull Constructor<@NotNull ByteReader> constructor) {
        return map.putIfAbsent(code, constructor);
    }

    public static @NotNull ByteReader get(byte code, int capacity) {
        if (!map.containsKey(code)) {
            throw new IllegalArgumentException("Cannot find the reader by the code: " + code);
        } else try {
            return map.get(code).newInstance(capacity);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("The constructor implementation throws an error: " + e.getCause().getMessage(), e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementation class", e);
        }
    }
}

package codes.shawlas.data.buffer.reader;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

final class BufferReaders {

    private static final @NotNull Map<@NotNull Byte, @NotNull Class<? extends BufferReader>> map = new HashMap<>();

    public static boolean contains(byte code) {
        return map.containsKey(code);
    }

    private static @NotNull Class<? extends BufferReader> get(byte code) {
        if (!map.containsKey(code)) {
            throw new IllegalArgumentException("Cannot find the reader by the code: " + code);
        }

        return map.get(code);
    }

    public static @NotNull BufferReader get(byte code, int capacity) {
        try {
            @NotNull Constructor<? extends BufferReader> constructor = get(code).getDeclaredConstructor(byte.class, int.class);
            constructor.setAccessible(true);
            return constructor.newInstance(code, capacity);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("The constructor implementation throws an error: " + e.getCause().getMessage(), e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementation class", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the constructor of the implementation class");
        }
    }

    public static @NotNull BufferReader get(byte @NotNull [] bytes, boolean independent) {
        try {
            @NotNull Constructor<? extends BufferReader> constructor = get(bytes[0]).getDeclaredConstructor(byte[].class, boolean.class);
            return constructor.newInstance(bytes, independent);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("The constructor implementation throws an error: " + e.getCause().getMessage(), e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate the implementation class", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the constructor of the implementation class");
        }
    }

    public static @NotNull BufferReader get(@NotNull ByteBuffer byteBuffer, boolean independent) {
        return get(byteBuffer.array(), independent);
    }
}

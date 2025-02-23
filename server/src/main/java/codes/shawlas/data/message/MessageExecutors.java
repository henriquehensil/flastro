package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.buffer.NoSuchBufferException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class MessageExecutors {

    private static final @NotNull Map<@NotNull Byte, @NotNull Class<? extends MessageExecutor>> map = new HashMap<>();

    private static @NotNull Class<? extends MessageExecutor> get(byte code) {
        if (!map.containsKey(code)) {
            throw new NoSuchBufferException("The reader code doest not exists");
        }

        return map.get(code);
    }

    static @NotNull MessageExecutor get(@NotNull Message.Input input, @NotNull Database database) {
        try {
            @NotNull Constructor<? extends MessageExecutor> constructor = get(input.getCode()).getDeclaredConstructor(Message.Input.class, Database.class);

            return constructor.newInstance(input, database);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the implementation constructor", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("A instantiation error occurs: " + e.getCause().getMessage(), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiates the implementation classes", e);
        }
    }

    // providers

    private static final class FileUploadExecutor extends MessageExecutor {

    }
}

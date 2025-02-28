package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.IllegalMessageException;
import codes.shawlas.data.exception.MessageExecutionException;
import codes.shawlas.data.impl.FileStorage;
import codes.shawlas.data.message.annotation.MessageCode;
import codes.shawlas.data.utils.MessageUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MessageProvider {

    private static final @NotNull Map<@NotNull Byte, @NotNull Class<? extends Message.Input>> map = new HashMap<>();

    static {
        for (@NotNull Class<?> aClass : MessageProvider.class.getDeclaredClasses()) {
            try {
                if (Message.Input.class.isAssignableFrom(aClass)) {
                    byte code = aClass.getDeclaredAnnotation(MessageCode.class).code();

                    // noinspection unchecked
                    map.put(code, (Class<? extends Message.Input>) aClass);
                }
            } catch (NullPointerException e) {
                throw new RuntimeException("Internal error, cannot find the message code from the message provider: " + aClass);
            }
        }
    }

    public static boolean contains(byte code) {
        return map.containsKey(code);
    }

    public static @NotNull Message.Input deserialize(@NotNull ByteBuffer buffer) {
        try {
            return (Message.Input) get(buffer.get(0)).getMethod("deserialize", ByteBuffer.class).invoke(null, buffer);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the deserializer method", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access the deserializer method", e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IllegalMessageException) {
                throw (IllegalMessageException) e.getCause();
            }

            throw new RuntimeException("Internal error", e);
        }
    }

    public static @NotNull Class<? extends Message.Input> get(byte code) {
        if (!contains(code)) {
            throw new IllegalArgumentException("The message code doest not exists");
        }

        return map.get(code);
    }

    // Implementations

    @MessageCode(code = 0x7)
    public static final class FileCreateMessage extends Message.Input {

        // Static initializer

        public static @NotNull FileCreateMessage deserialize(@NotNull ByteBuffer buffer) {
            byte code = buffer.get(0);

            if (code != (byte) 0x7) {
                throw new IllegalMessageException("Cannot deserialize the buffer because the code '" + code + "' is not supported");
            } else try {
                @NotNull UUID id = MessageUtils.getId(buffer);
                @NotNull OffsetDateTime time = MessageUtils.getTime(buffer);
                @NotNull Path path = MessageUtils.getPath(buffer);
                @NotNull String name = MessageUtils.getString(buffer);

                return new FileCreateMessage(id, time, path, name);
            } catch (Throwable e) {
                throw new IllegalMessageException("Message has illegal format", e);
            }
        }

        // Objects

        private final @NotNull Path path;
        private final @NotNull String name;

        private FileCreateMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Path path, @NotNull String name) {
            super(id, time, (byte) 0x7);
            this.path = path;
            this.name = name;
        }

        public @NotNull Path getPath() {
            return path;
        }

        public @NotNull Path getCompletePath() {
            return path.resolve(name);
        }

        public @NotNull String getName() {
            return name;
        }

        @Override
        public void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel) {
            try {
                @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();
                manager.createFile(this.getCompletePath());
            } catch (Throwable e) {
                throw new MessageExecutionException("An error occurred while creating the file", e);
            }
        }
    }

    @MessageCode(code = 0x5)
    public static final class FileDeleteMessage extends Message.Input {

        // Static initializers

        public static @NotNull FileDeleteMessage deserialize(@NotNull ByteBuffer buffer) {
            byte code = buffer.get(0);

            if (code != (byte) 0x5) {
                throw new IllegalMessageException("Cannot deserialize the buffer because the code '" + code + "' is not supported");
            } else try {
                @NotNull UUID id = MessageUtils.getId(buffer);
                @NotNull OffsetDateTime time = MessageUtils.getTime(buffer);
                @NotNull Path path = MessageUtils.getPath(buffer);
                @NotNull String name = MessageUtils.getString(buffer);

                return new FileDeleteMessage(id, time, path, name);
            } catch (Throwable e) {
                throw new IllegalMessageException("Message has illegal format", e);
            }
        }

        // Objects

        private final @NotNull Path path;
        private final @NotNull String name;

        private FileDeleteMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Path path, @NotNull String name) {
            super(id, time, (byte) 0x5);
            this.path = path;
            this.name = name;
        }

        public @NotNull Path getPath() {
            return path;
        }

        public @NotNull String getName() {
            return name;
        }

        @Override
        public void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel) {
            try {
                @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();
                manager.delete(path);
            } catch (Throwable e) {
                throw new MessageExecutionException("An error occurred while creating the file", e);
            }
        }
    }

    @MessageCode(code = 0x8)
    public static final class FileUploadMessage extends Message.Input {

        // Static initializer

        public static @NotNull FileUploadMessage deserialize(@NotNull ByteBuffer buffer) {
            byte code = buffer.get(0);

            if (code != (byte) 0x8) {
                throw new IllegalMessageException("Cannot deserialize the buffer because the code '" + code + "' is not supported");
            } else try {
                @NotNull UUID id = MessageUtils.getId(buffer);
                @NotNull OffsetDateTime time = MessageUtils.getTime(buffer);
                @NotNull Path path = MessageUtils.getPath(buffer);
                @NotNull String name = MessageUtils.getString(buffer);
                int size = buffer.getInt();

                return new FileUploadMessage(id, time, path, name, size);
            } catch (Throwable e) {
                throw new IllegalMessageException("Message has illegal format", e);
            }
        }

        // Objects

        private final @NotNull Path path;
        private final @NotNull String name;
        private final int size;

        private FileUploadMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Path path, @NotNull String name, int size) {
            super(id, time, (byte) 0x8);
            this.path = path;
            this.name = name;
            this.size = size;
        }

        public @NotNull Path getPath() {
            return path;
        }

        public @NotNull Path getCompletePath() {
            return path.resolve(path);
        }

        public @NotNull String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        @Override
        public void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel) {
            if (buffer == null || channel == null) {
                throw new UnsupportedOperationException("Buffer and channel cannot be null");
            }

            @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();

            try (FileOutputStream out = manager.createFile(path)) {
                @NotNull FileChannel fileChannel = out.getChannel();
                @NotNull FileLock lock = fileChannel.lock();

                int writes = fileChannel.write(buffer);

                while (writes < size) {
                    buffer.clear();
                    channel.read(buffer);
                    buffer.flip();
                    writes += fileChannel.write(buffer);
                }

                lock.release();
                channel.close();
            } catch (Throwable e) {
                throw new MessageExecutionException("An error occurred while creating the file", e);
            }
        }
    }

    @MessageCode(code = 0x6)
    public static final class FolderCreateMessage extends Message.Input {

        // Static initializer

        public static @NotNull FolderCreateMessage deserialize(@NotNull ByteBuffer buffer) {
            byte code = buffer.get(0);

            if (code != (byte) 0x7) {
                throw new IllegalMessageException("Cannot deserialize the buffer because the code '" + code + "' is not supported");
            } else try {
                @NotNull UUID id = MessageUtils.getId(buffer);
                @NotNull OffsetDateTime time = MessageUtils.getTime(buffer);
                @NotNull Path path = MessageUtils.getPath(buffer);

                return new FolderCreateMessage(id, time, path);
            } catch (Throwable e) {
                throw new IllegalMessageException("Message has illegal format", e);
            }
        }

        // Objects

        private final @NotNull Path paths;

        private FolderCreateMessage(@NotNull UUID id, @NotNull OffsetDateTime time, @NotNull Path paths) {
            super(id, time, (byte) 0x6);
            this.paths = paths;
        }

        public @NotNull Path getPaths() {
            return paths;
        }

        @Override
        public void execute(@NotNull Database database, @Nullable ByteBuffer buffer, @Nullable ReadableByteChannel channel) {
            try {
                @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();
                manager.createFile(paths);
            } catch (Throwable e) {
                throw new MessageExecutionException("An error occurred while creating the file", e);
            }
        }
    }
}
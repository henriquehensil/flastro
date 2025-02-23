package codes.shawlas.data.message;

import codes.shawlas.data.exception.message.MessageReaderException;
import codes.shawlas.data.exception.message.NoSuchReaderException;
import codes.shawlas.data.message.annotation.ByteCode;
import codes.shawlas.data.message.content.file.FileCreateMessage;
import codes.shawlas.data.message.content.file.FileDeleteMessage;
import codes.shawlas.data.message.content.file.FileUploadMessage;
import codes.shawlas.data.message.content.file.FolderCreateMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

final class MessageReaders {

    // Map

    private static final @NotNull Map<@NotNull Byte, @NotNull Class<? extends MessageReader>> map = new HashMap<>();

    static {
        for (@NotNull Class<?> c : MessageReaders.class.getDeclaredClasses()) {
            if (MessageReader.class.isAssignableFrom(c)) {
                byte code = c.getDeclaredAnnotation(ByteCode.class).value();
                //noinspection unchecked
                map.put(code, (Class<? extends MessageReader>) c);
            }
        }
    }

    private static @NotNull Class<? extends MessageReader> get(byte code) {
        if (!map.containsKey(code)) {
            throw new NoSuchReaderException("The reader code doest not exists");
        }

        return map.get(code);
    }

    static @NotNull MessageReader get(@NotNull ByteBuffer buffer) {
        try {
            byte code = buffer.get(0);
            @NotNull Constructor<? extends MessageReader> constructor = get(code).getDeclaredConstructor(ByteBuffer.class);

            return constructor.newInstance(buffer);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find the implementation constructor", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("A instantiation error occurs: " + e.getCause().getMessage(), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiates the implementation classes", e);
        }
    }

    // providers

    @ByteCode(0x8)
    private static final class FileUploadReader extends MessageReader {

        private FileUploadReader(@NotNull ByteBuffer buffer) {
            super((byte) 0x8, buffer);
        }

        @Override
        public @NotNull CompletableFuture<Message.Input> nextMessage() {
            @NotNull CompletableFuture<Message.Input> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    @NotNull UUID id = nextId();
                    @NotNull OffsetDateTime time = nextTime();
                    @NotNull Path path = nextPath();
                    @NotNull String name = nextString();
                    int size = nextInt();

                    future.complete(new FileUploadMessage(id, time, path, name, size));
                } catch (BufferUnderflowException e) {
                   future.completeExceptionally(new MessageReaderException("It's not possible to continue reading", e));
                }
            });

            return future;
        }
    }

    @ByteCode(0x7)
    public static final class FileCreateReader extends MessageReader {

        private FileCreateReader(@NotNull ByteBuffer buffer) {
            super((byte) 0x7, buffer);
        }

        @Override
        public @NotNull CompletableFuture<Message.Input> nextMessage() {
            @NotNull CompletableFuture<Message.Input> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    @NotNull UUID id = nextId();
                    @NotNull OffsetDateTime time = nextTime();
                    @NotNull Path path = nextPath();
                    @NotNull String name = nextString();

                    future.complete(new FileCreateMessage(id, time, path, name));
                } catch (BufferUnderflowException e) {
                    future.completeExceptionally(new MessageReaderException("It's not possible to continue reading", e));
                }
            });

            return future;
        }
    }

    // Implementations

    @ByteCode(0X6)
    private static final class FolderCreateReader extends MessageReader {

        private FolderCreateReader(byte code, @NotNull ByteBuffer buffer) {
            super((byte) 0X6, buffer);
        }

        @Override
        public @NotNull CompletableFuture<Message.Input> nextMessage() {
            @NotNull CompletableFuture<Message.Input> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    @NotNull UUID id = nextId();
                    @NotNull OffsetDateTime time = nextTime();
                    @NotNull Path path = nextPath();

                    future.complete(new FolderCreateMessage(id, time, path));
                } catch (BufferUnderflowException e) {
                    future.completeExceptionally(new MessageReaderException("It's not possible to continue reading", e));
                }
            });

            return future;
        }
    }

    @ByteCode(0X5)
    private static final class FileDeleteReader extends MessageReader {

        private FileDeleteReader(byte code, @NotNull ByteBuffer buffer) {
            super((byte) 0X6, buffer);
        }

        @Override
        public @NotNull CompletableFuture<Message.Input> nextMessage() {
            @NotNull CompletableFuture<Message.Input> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    @NotNull UUID id = nextId();
                    @NotNull OffsetDateTime time = nextTime();
                    @NotNull Path path = nextPath();
                    @NotNull String name = nextString();

                    future.complete(new FileDeleteMessage(id, time, path, name));
                } catch (BufferUnderflowException e) {
                    future.completeExceptionally(new MessageReaderException("It's not possible to continue reading", e));
                }
            });

            return future;
        }
    }
}
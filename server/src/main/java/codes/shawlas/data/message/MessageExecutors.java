package codes.shawlas.data.message;

import codes.shawlas.data.database.Database;
import codes.shawlas.data.exception.message.MessageExecutorException;
import codes.shawlas.data.exception.message.NoSuchExecutorException;
import codes.shawlas.data.impl.FileStorage;
import codes.shawlas.data.message.annotation.MessageDependency;
import codes.shawlas.data.message.content.file.FileCreateMessage;
import codes.shawlas.data.message.content.file.FileDeleteMessage;
import codes.shawlas.data.message.content.file.FileUploadMessage;
import codes.shawlas.data.message.content.file.FolderCreateMessage;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class MessageExecutors {

    private static final @NotNull Map<@NotNull Class<? extends Message.Input>, @NotNull Class<? extends MessageExecutor>> map = new HashMap<>();

    static {
        for (@NotNull Class<?> c : MessageExecutors.class.getDeclaredClasses()) {
            if (MessageExecutor.class.isAssignableFrom(c)) {
                @NotNull Class<? extends Message.Input> messageClass = c.getDeclaredAnnotation(MessageDependency.class).value();
                //noinspection unchecked
                map.put(messageClass, (Class<? extends MessageExecutor>) c);
            }
        }
    }

    private static @NotNull Class<? extends MessageExecutor> get(@NotNull Class<? extends Message.Input> code) {
        if (!map.containsKey(code)) {
            throw new NoSuchExecutorException("The reader code doest not exists");
        }

        return map.get(code);
    }

    static @NotNull MessageExecutor get(@NotNull Message.Input input, @NotNull Database database) {
        try {
            @NotNull Class<? extends Message.Input> messageClass = input.getClass();
            @NotNull Constructor<? extends MessageExecutor> constructor = get(messageClass).getDeclaredConstructor(Message.Input.class, Database.class);

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

    @MessageDependency(FileCreateMessage.class)
    private static final class FileCreateExecutor extends MessageExecutor {

        private FileCreateExecutor(@NotNull Database database, Message.@NotNull Input message) {
            super(database, message);
        }

        @Override
        public @NotNull CompletableFuture<Void> execute(@NotNull ByteBuffer buffer, @NotNull ReadableByteChannel channel) {
            return CompletableFuture.runAsync(() -> {
                try {
                    @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();
                    @NotNull Path path = ((FileCreateMessage) this.message).getCompletePath();
                    manager.createFile(path);
                } catch (Throwable e) {
                    throw new MessageExecutorException("An error occurred while creating the file");
                }
            });
        }
    }

    @MessageDependency(FolderCreateMessage.class)
    private static final class FolderCreateExecutor extends MessageExecutor {

        private FolderCreateExecutor(@NotNull Database database, Message.@NotNull Input message) {
            super(database, message);
        }

        @Override
        public @NotNull CompletableFuture<Void> execute(@NotNull ByteBuffer buffer, @NotNull ReadableByteChannel channel) {
            return CompletableFuture.runAsync(() -> {
                try {
                    @NotNull FileStorage.FileManager manager = database.getStorages().getFileStorage().getManager();
                    @NotNull Path path = ((FolderCreateMessage) this.message).getPaths();

                    manager.createFile(path);
                } catch (Throwable e) {
                    throw new MessageExecutorException("An error occurred while creating the file");
                }
            });
        }
    }

    @MessageDependency(FileUploadMessage.class)
    private static final class FileUploadExecutor extends MessageExecutor {

        private FileUploadExecutor(@NotNull Database database, Message.@NotNull Input message) {
            super(database, message);
        }

        @Override
        public @NotNull CompletableFuture<Void> execute(@NotNull ByteBuffer buffer, @NotNull ReadableByteChannel channel) {
            return CompletableFuture.runAsync(() -> {
                @NotNull FileStorage.FileManager manager = this.database.getStorages().getFileStorage().getManager();
                @NotNull FileUploadMessage message = (FileUploadMessage) this.message;
                @NotNull Path path = message.getCompletePath();
                int size = message.getSize();

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
                    throw new MessageExecutorException("An error occurred while creating the file");
                }
            });
        }
    }

    @MessageDependency(FileDeleteMessage.class)
    private static final class DeleteFileExecutor extends MessageExecutor {

        private DeleteFileExecutor(@NotNull Database database, Message.@NotNull Input message) {
            super(database, message);
        }

        @Override
        public @NotNull CompletableFuture<Void> execute(@NotNull ByteBuffer buffer, @NotNull ReadableByteChannel channel) {
            return CompletableFuture.runAsync(() -> {
                @NotNull FileStorage.FileManager manager = this.database.getStorages().getFileStorage().getManager();
                @NotNull Path path = ((FileUploadMessage) this.message).getCompletePath();

                try {
                    manager.delete(path);
                } catch (Throwable e) {
                    throw new MessageExecutorException("An error occurred while creating the file");
                }
            });
        }
    }
}
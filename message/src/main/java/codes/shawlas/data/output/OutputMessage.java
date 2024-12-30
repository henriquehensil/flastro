package codes.shawlas.data.output;

import codes.shawlas.data.Message;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface OutputMessage extends Message {

    @Blocking
    default void write(@NotNull OutputStream output) throws IOException {
        output.write(getModel().getBytes());
    }

    @Blocking
    default void write(@NotNull ByteBuffer buffer) {
        buffer.put(getModel().getBytes());
    }

    @NotNull JsonObject serialize();
}

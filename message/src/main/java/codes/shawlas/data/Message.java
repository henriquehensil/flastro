package codes.shawlas.data;

import codes.shawlas.data.exception.MessageFinishException;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface Message {

    @NotNull MessageModel getModel();

    // Classes

    interface Input extends Message {

        /**
         * @return An OutputMessage based on this input message
         *
         * @throws MessageFinishException if message was finished
         * */
        @NotNull Message.Output execute() throws MessageFinishException;
    }

    interface Output extends Message {

        @Blocking
        default void write(@NotNull OutputStream output) throws IOException {
            output.write(getModel().getFull().getBytes());
        }

        @Blocking
        default void write(@NotNull ByteBuffer buffer) {
            buffer.put(getModel().getFull().getBytes());
        }

        /**
         * @return The data encapsulate in Json
         * */
        @NotNull JsonObject serialize();
    }
}

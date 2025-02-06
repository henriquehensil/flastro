package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public sealed interface Message permits Message.Input, Message.Output {

    @Range(from = 0, to = Long.MAX_VALUE) int getId();

    byte @NotNull [] getBytes();

    // Classes

    non-sealed abstract class Input implements Message {

        private final int id;

        public Input(int id) {
            if (id <= 0) throw new IllegalArgumentException("Invalid id");
            this.id = id;
        }

        @Override
        public final @Range(from = 0, to = Long.MAX_VALUE) int getId() {
            return id;
        }

        /**
         * @return  The output processed or null if the process is not needed
         * */
        public abstract @Nullable Output process();

        @Override
        public final boolean equals(@Nullable Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Input input = (Input) object;
            return id == input.id;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(id);
        }
    }

    non-sealed abstract class Output implements Message {

        private final int id;

        public Output(int id) {
            if (id <= 0) throw new IllegalArgumentException("Invalid id");
            this.id = id;
        }

        @Override
        public final @Range(from = 0, to = Long.MAX_VALUE) int getId() {
            return id;
        }

        public abstract void write(@NotNull OutputStream output) throws IOException;

        @Override
        public final boolean equals(@Nullable Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            @NotNull Output output = (Output) object;
            return id == output.id;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(id);
        }
    }
}

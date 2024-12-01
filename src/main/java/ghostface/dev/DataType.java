package ghostface.dev;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public interface DataType<T> {

    @NotNull DataType<String> STRING = new DataType<String>() {
        @Override
        public @NotNull String read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readUTF();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull String data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeUTF(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<String> getType() {
            return String.class;
        }
    };
    @NotNull DataType<Byte> BYTE = new DataType<Byte>() {
        @Override
        public @NotNull Byte read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readByte();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Byte data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeByte(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Byte> getType() {
            return Byte.class;
        }
    };
    @NotNull DataType<Short> SHORT = new DataType<Short>() {
        @Override
        public @NotNull Short read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readShort();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Short data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeShort(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Short> getType() {
            return Short.class;
        }
    };
    @NotNull DataType<Integer> INTEGER = new DataType<Integer>() {
        @Override
        public @NotNull Integer read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readInt();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Integer data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeInt(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Integer> getType() {
            return Integer.class;
        }
    };
    @NotNull DataType<Long> LONG = new DataType<Long>() {
        @Override
        public @NotNull Long read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readLong();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Long data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeLong(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Long> getType() {
            return Long.class;
        }
    };
    @NotNull DataType<Float> FLOAT = new DataType<Float>() {
        @Override
        public @NotNull Float read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readFloat();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Float data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeFloat(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Float> getType() {
            return Float.class;
        }
    };
    @NotNull DataType<Double> DOUBLE = new DataType<Double>() {
        @Override
        public @NotNull Double read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readDouble();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Double data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeDouble(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Double> getType() {
            return Double.class;
        }
    };
    @NotNull DataType<Boolean> BOOLEAN = new DataType<Boolean>() {
        @Override
        public @NotNull Boolean read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readBoolean();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Boolean data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeBoolean(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Boolean> getType() {
            return Boolean.class;
        }
    };
    @NotNull DataType<Character> CHAR = new DataType<Character>() {
        @Override
        public @NotNull Character read(@NotNull InputStream stream) throws IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readChar();
            }
        }

        @Override
        public void write(@NotNull OutputStream outputStream, @NotNull Character data) throws IOException {
            try (@NotNull DataOutputStream writer = new DataOutputStream(outputStream)) {
                writer.writeChar(data);
                writer.flush();
            }
        }

        @Override
        public @NotNull Class<Character> getType() {
            return Character.class;
        }
    };

    // Objects

    /**
     * @throws IOException if an I/O exception occurs while reading.
     * */
    @NotNull T read(@NotNull InputStream inputStream) throws IOException;

    /**
     * @throws IOException if an I/O exception occurs while writing.
     * */
    void write(@NotNull OutputStream outputStream, @NotNull T data) throws IOException;

    @NotNull Class<T> getType();
}

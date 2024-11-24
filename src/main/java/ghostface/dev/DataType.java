package ghostface.dev;

import ghostface.dev.exception.DataTypeException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface DataType<T> {

    @NotNull DataType<String> STRING = new DataType<String>() {
        @Override
        public @NotNull String read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull InputStreamReader data = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                @NotNull StringBuilder builder = new StringBuilder();
                char @NotNull [] chars = new char[1024];

                int read = data.read(chars);

                while (read > 0) {
                    builder.append(chars, 0, read);
                    read = data.read(chars);
                }

                if (builder.toString().trim().isEmpty()) {
                    throw new DataTypeException("Data is null");
                }

                return builder.toString();
            }
        }

        @Override
        public @NotNull Class<String> getType() {
            return String.class;
        }
    };

    @NotNull DataType<Byte> BYTE = new DataType<Byte>() {
        @Override
        public @NotNull Byte read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readByte();
            } catch (EOFException e) {
                throw new DataTypeException("No Byte found", e);
            }
        }

        @Override
        public @NotNull Class<Byte> getType() {
            return Byte.class;
        }
    };

    @NotNull DataType<Short> SHORT = new DataType<Short>() {
        @Override
        public @NotNull Short read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readShort();
            } catch (EOFException e) {
                throw new DataTypeException("No Short found", e);
            }
        }

        @Override
        public @NotNull Class<Short> getType() {
            return Short.class;
        }
    };

    @NotNull DataType<Integer> INTEGER = new DataType<Integer>() {
        @Override
        public @NotNull Integer read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readInt();
            } catch (EOFException e) {
                throw new DataTypeException("No Integer found", e);
            }
        }

        @Override
        public @NotNull Class<Integer> getType() {
            return Integer.class;
        }
    };

    @NotNull DataType<Long> LONG = new DataType<Long>() {
        @Override
        public @NotNull Long read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readLong();
            } catch (EOFException e) {
                throw new DataTypeException("No Long found", e);
            }
        }

        @Override
        public @NotNull Class<Long> getType() {
            return Long.class;
        }
    };

    @NotNull DataType<Float> FLOAT = new DataType<Float>() {
        @Override
        public @NotNull Float read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readFloat();
            } catch (EOFException e) {
                throw new DataTypeException("No Float found", e);
            }
        }

        @Override
        public @NotNull Class<Float> getType() {
            return Float.class;
        }
    };

    @NotNull DataType<Double> DOUBLE = new DataType<Double>() {
        @Override
        public @NotNull Double read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readDouble();
            } catch (EOFException e) {
                throw new DataTypeException("No Double found", e);
            }
        }

        @Override
        public @NotNull Class<Double> getType() {
            return Double.class;
        }
    };

    @NotNull DataType<Boolean> BOOLEAN = new DataType<Boolean>() {
        @Override
        public @NotNull Boolean read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readBoolean();
            } catch (EOFException e) {
                throw new DataTypeException("No Boolean found", e);
            }
        }

        @Override
        public @NotNull Class<Boolean> getType() {
            return Boolean.class;
        }
    };

    @NotNull DataType<Character> CHAR = new DataType<Character>() {
        @Override
        public @NotNull Character read(@NotNull InputStream stream) throws DataTypeException, IOException {
            try (@NotNull DataInputStream data = new DataInputStream(stream)) {
                return data.readChar();
            } catch (EOFException e) {
                throw new DataTypeException("No Character found", e);
            }
        }

        @Override
        public @NotNull Class<Character> getType() {
            return Character.class;
        }
    };

    // Objects

    /**
     * @throws DataTypeException if data is not a valid DataType
     * */
    @NotNull T read(@NotNull InputStream stream) throws DataTypeException, IOException;

    @NotNull Class<T> getType();
}

package codes.shawlas.data.io;

import codes.shawlas.data.nest.Nest;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public interface DataTypeInput extends DataInput, Closeable, Flushable {

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull LocalDate readDate() throws IOException;

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull LocalTime readTime() throws IOException;

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull OffsetDateTime readOffsetDate() throws IOException;

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull File readFile() throws IOException;

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull Nest<?> readNest() throws IOException;

    /**
     * @throws java.io.EOFException if this stream reaches the end before reading all the bytes.
     * */
    @NotNull JsonElement readJson() throws IOException;

}
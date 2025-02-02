package codes.shawlas.data.io;

import codes.shawlas.data.nest.Nest;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public interface DataTypeOutput extends DataOutput, Closeable, Flushable {

    void writeDate(@NotNull LocalDate date) throws IOException;

    void writeTime(@NotNull LocalTime time) throws IOException;

    void writeOffsetDate(@NotNull OffsetDateTime offsetDate) throws IOException;

    void writeFile(@NotNull FileInputStream inputStream) throws IOException;

    void write(@NotNull Nest<?> nest) throws IOException;

    void writeJson(@NotNull JsonElement element) throws IOException;

}
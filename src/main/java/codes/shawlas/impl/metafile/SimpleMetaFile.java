package codes.shawlas.impl.metafile;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public final class SimpleMetaFile extends AbstractMetaFile {

    public SimpleMetaFile(@NotNull File file) throws IOException {
        super(file, BasicFileAttributes.class);
    }

    @Override
    public @NotNull JsonObject serialize() {
        return jsonProvider(this);
    }
}

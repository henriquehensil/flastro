package codes.shawlas.impl.metafile;

import codes.shawlas.file.AbstractMetaFile;
import codes.shawlas.file.MetaFile;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public final class SimpleMetaFile extends AbstractMetaFile {

    // Static initializer
    static @NotNull JsonObject jsonProvider(@NotNull MetaFile metaFile) {
        @NotNull JsonObject general = new JsonObject();
        general.addProperty("name", metaFile.getPath().getFileName().toString());
        general.addProperty("key", metaFile.getKey());
        general.addProperty("size", metaFile.getData().size());
        general.addProperty("path", metaFile.getPath().toString());

        @NotNull JsonObject times = new JsonObject();
        times.addProperty("created", metaFile.getTimes().getCreated().toString());
        times.addProperty("modified", metaFile.getTimes().getModified().toString());
        times.addProperty("access", metaFile.getTimes().getAccess().toString());

        @NotNull JsonObject permissions = new JsonObject();
        permissions.addProperty("readable", metaFile.getPermissions().isFullyReadable());
        permissions.addProperty("writable", metaFile.getPermissions().isFullyWritable());
        permissions.addProperty("executable", metaFile.getPermissions().isFullyExecutable());

        @NotNull JsonObject object = new JsonObject();
        object.add("general", general);
        object.add("times", times);
        object.add("permissions", permissions);

        return object;
    }

    // Objects

    public SimpleMetaFile(@NotNull File file) throws IOException {
        super(file, Files.readAttributes(file.toPath(), BasicFileAttributes.class));
    }

    @Override
    public @NotNull JsonObject serialize() {
        return jsonProvider(this);
    }
}

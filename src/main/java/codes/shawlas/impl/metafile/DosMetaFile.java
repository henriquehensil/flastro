package codes.shawlas.impl.metafile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.DosFileAttributes;

public final class DosMetaFile extends MetaFileImpl {

    private final @NotNull DosFileAttributes dosAttr;
    private final @NotNull DosMetaFileAttributes dosMetaAttr = new DosMetaFileAttributes();

    public DosMetaFile(@NotNull File file) throws IOException {
        super(file, DosFileAttributes.class);
        this.dosAttr = (DosFileAttributes) basicAttr;
    }

    @Override
    public @NotNull JsonElement serialize() {
        @NotNull JsonObject object = super.serialize().getAsJsonObject();

        object.add("fat", getDosAttributes().serialize());

        return object;
    }

    public @NotNull DosMetaFileAttributes getDosAttributes() {
        return dosMetaAttr;
    }

    // Classes

    public final class DosMetaFileAttributes {

        private @NotNull JsonObject serialize() {
            @NotNull JsonObject object = new JsonObject();

            object.addProperty("archive", isArchive());
            object.addProperty("hidden", isHidden());
            object.addProperty("readyOnly", isReadOnly());
            object.addProperty("system", isSystem());

            return object;
        }

        public boolean isArchive() {
            return dosAttr.isArchive();
        }

        public boolean isHidden() {
            return dosAttr.isHidden();
        }

        public boolean isReadOnly() {
            return dosAttr.isReadOnly();
        }

        public boolean isSystem() {
            return dosAttr.isSystem();
        }
    }
}

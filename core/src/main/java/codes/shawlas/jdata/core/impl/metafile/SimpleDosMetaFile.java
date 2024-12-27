package codes.shawlas.jdata.core.impl.metafile;

import codes.shawlas.jdata.core.file.DosMetaFile;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.DosFileAttributes;

public final class SimpleDosMetaFile extends AbstractMetaFile implements DosMetaFile {

    private final @NotNull DosFileAttributes attributes;
    private final @NotNull FatAttributes fat = new FatAttributesImpl();

    public SimpleDosMetaFile(@NotNull File file) throws IOException {
        super(file, DosFileAttributes.class);
        this.attributes = (DosFileAttributes) super.attributes;
    }

    @Override
    public @NotNull JsonObject serialize() {
        @NotNull JsonObject fat = new JsonObject();
        fat.addProperty("archive", getFatAttributes().isArchive());
        fat.addProperty("hidden", getFatAttributes().isHidden());
        fat.addProperty("readyOnly", getFatAttributes().isReadOnly());
        fat.addProperty("system", getFatAttributes().isSystem());

        @NotNull JsonObject object = SimpleMetaFile.jsonProvider(this);
        object.add("fat", fat);

        return object;
    }

    @Override
    public @NotNull FatAttributes getFatAttributes() {
        return fat;
    }

    // Classes
    private final class FatAttributesImpl implements FatAttributes {
        @Override
        public boolean isArchive() {
            return attributes.isArchive();
        }

        @Override
        public boolean isHidden() {
            return attributes.isHidden();
        }

        @Override
        public boolean isReadOnly() {
            return attributes.isReadOnly();
        }

        @Override
        public boolean isSystem() {
            return attributes.isSystem();
        }
    }
}
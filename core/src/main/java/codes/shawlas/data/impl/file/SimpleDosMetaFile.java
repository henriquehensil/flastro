package codes.shawlas.data.impl.file;

import codes.shawlas.data.file.DosMetaFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.DosFileAttributes;

final class SimpleDosMetaFile extends AbstractMetaFile implements DosMetaFile {

    private final @NotNull DosFileAttributes attributes;
    private final @NotNull FatAttributes fat = new Attributes();

    private SimpleDosMetaFile(@NotNull File file) throws IOException {
        super(file, DosFileAttributes.class);
        this.attributes = (DosFileAttributes) super.attributes;
    }

    @Override
    public @NotNull FatAttributes getFatAttributes() {
        return fat;
    }

    // Classes

    private final class Attributes implements FatAttributes {
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

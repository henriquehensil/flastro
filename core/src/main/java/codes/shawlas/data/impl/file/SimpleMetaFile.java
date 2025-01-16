package codes.shawlas.data.impl.file;

import codes.shawlas.data.file.MetaFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

final class SimpleMetaFile extends AbstractMetaFile implements MetaFile {
    private SimpleMetaFile(@NotNull File file) throws IOException {
        super(file, BasicFileAttributes.class);
    }
}

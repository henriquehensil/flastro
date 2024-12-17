package codes.shawlas;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;

public final class Main {
    public static void main(String[] args) throws IOException {
        @NotNull File file = new File("/home/alana/Área de trabalho", "test.txt");

        file.createNewFile();

        System.out.println(Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class).readAttributes().fileKey().getClass().getName());
    }
}

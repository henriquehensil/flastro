package ghostface.dev;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        @NotNull Path path = Paths.get("test");

        System.out.println(path);
        System.out.println(path.isAbsolute());
        System.out.println(path.toFile().isDirectory());
        System.out.println("exists: " + path.toFile().exists());

        /*
        * pegue o folder
        * */
    }
}

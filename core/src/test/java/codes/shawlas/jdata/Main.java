package codes.shawlas.jdata;

import org.jetbrains.annotations.NotNull;

public final class Main {
    public static void main(@NotNull String @NotNull [] args) throws ClassNotFoundException {
        // load fields
        Class.forName(FieldsProviders.class.getName());
    }

    private Main() {
    }
}

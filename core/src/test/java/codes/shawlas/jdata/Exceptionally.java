package codes.shawlas.jdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface Exceptionally {
    @Test
    @DisplayName("Tests that all common errors are working")
    void commonExceptions() throws Throwable;
}

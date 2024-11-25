package ghostface.dev.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface UnmodifiableContent<T> {

    @Unmodifiable @NotNull Collection<? extends T> toCollection();
}

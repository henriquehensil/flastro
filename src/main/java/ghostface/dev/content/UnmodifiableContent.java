package ghostface.dev.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Iterator;

public interface UnmodifiableContent<T> extends Iterable<T> {

    @Override
    default @NotNull Iterator<T> iterator() {
        return toCollection().iterator();
    }

    @Unmodifiable @NotNull Collection<T> toCollection();
}

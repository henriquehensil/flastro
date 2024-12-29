package codes.shawlas.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface Message {

    @NotNull Action getAction();

    @NotNull Item getItem();

    @Range(from = 0, to = Long.MAX_VALUE) int getId();

    // Classes

    enum Action {
        CREATE,
        DELETE,
        UPDATE,
        GET;
    }

    enum Item {
        FILE,
        TABLE,
        ELEMENT,
        COLUMN,
        NEST,
    }
}

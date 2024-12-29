package codes.shawlas.data.input;

import codes.shawlas.data.Message;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface InputMessage extends Message {

    /**
     * @return The {@code Action} that output message represents
     * */
    @Override
    @NotNull Action getAction();

    /**
     * @return The {@code Item} that the output message wants to use to perform operations
     * */
    @Override
    @NotNull Item getItem();

    /**
     * @return The identifier that output message represents
     * */
    @Override
    @Range(from = 0, to = Long.MAX_VALUE) int getId();

    /**
     * @return The Json data of the output message
     * */
    @NotNull JsonObject getData();
}

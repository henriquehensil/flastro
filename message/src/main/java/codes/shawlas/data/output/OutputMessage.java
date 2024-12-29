package codes.shawlas.data.output;

import codes.shawlas.data.Message;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface OutputMessage extends Message {

    /**
     * @return The {@code Action} that represents this output message
     * */
    @Override
    @NotNull Action getAction();

    /**
     * @return The {@code Item} used for the operations and which represents this output message
     * */
    @Override
    @NotNull Item getItem();

    /**
     * @return The identifier that represents this output message
     * */
    @Override
    @Range(from = 0, to = Long.MAX_VALUE) int getId();

    /**
     * @return The Json that contains all the data of this output message
     * */
    @NotNull JsonObject serialize();
}
